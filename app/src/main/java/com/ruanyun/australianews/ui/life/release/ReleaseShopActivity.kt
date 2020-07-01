package com.ruanyun.australianews.ui.life.release

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.*
import com.ruanyun.australianews.model.AddressInfo
import com.ruanyun.australianews.model.LifeFoodShopInfo
import com.ruanyun.australianews.model.ParentCodeInfo
import com.ruanyun.australianews.model.params.ReleaseLifeShopParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.ui.MultipleSelectionActivity
import com.ruanyun.australianews.ui.my.HeaderPickerActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.SelectDateTimeDialog
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import com.ruanyun.imagepicker.AndroidImagePicker
import com.ruanyun.imagepicker.bean.ImageItem
import com.ruanyun.imagepicker.ui.ImagesGridActivity
import com.ruanyun.imagepicker.ui.ImagesGridActivity.SELECTED_PIC
import kotlinx.android.synthetic.main.activity_release_life_shop.*
import kotlinx.android.synthetic.main.layout_life_release_contact.*
import java.io.File
import javax.inject.Inject

/**
 * @description 发布餐饮美食信息
 * @author hdl
 * @date 2019/11/12
 */
class ReleaseShopActivity : ReleaseBaseActivity(), AndroidImagePicker.OnPictureTakeCompleteListener {
    override fun onPictureTakeComplete(picturePath: String?, requestCode: Int) {
        picturePath?:return
        iv_add_photo.loadImageNoDefault(File(picturePath))
        val imageItemList = mutableListOf<ImageItem>()
        imageItemList.add(ImageItem(picturePath))
        ryPicView?.refreshImage(imageItemList)
    }

    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getLifeFoodShopInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeFoodShopInfo>>() {
                override fun onSuccess(result: ResultBase<LifeFoodShopInfo>) {
                    disMissLoadingView()
                    val t = result.data
//                    setReleasePhoto(t.mainPhoto, t.title)
//                    setReleaseDetails(t.remark)
                    setReleaseContact(t.linkMan, t.linkTel, t.email, t.qq, t.weixin)
                    iv_add_photo.loadImage(t.mainPhoto?.toImgUrl())
                    val imageItemList = mutableListOf<ImageItem>()
                    imageItemList.add(ImageItem(t.mainPhoto, t.mainPhoto?.toImgUrl(), ImageItem.TYPE_REMOTE))
                    ryPicView?.refreshImage(imageItemList)
                    et_shop_name.setText(t.title)
                    tv_cuisine.text = DbHelper.getInstance().getItemNames(t.cookStyle, C.ParentCode.COOK_STYLE)
                    params.cookStyle = t.cookStyle
                    tv_city.text = t.city
                    params.city = t.city
                    et_shop_address.setText(t.address)
                    tv_business_cycle.text = DbHelper.getInstance().getItemNames(t.workPeriod, C.ParentCode.WORK_PERIOD)
                    params.workPeriod = t.workPeriod
                    tv_opening_hours.text = StringUtil.getTimeStrFromFormatStr("HH:mm", t.workStartTime)
                    params.workStartTimes = tv_opening_hours.getStr()
                    tv_business_end_time.text = StringUtil.getTimeStrFromFormatStr("HH:mm", t.workEndTime)
                    params.workEndTimes = tv_business_end_time.getStr()
                    if(!isNegotiable(t.startPrice)) {
                        et_starting_price.setText(t.startPrice)
                    }
                    if(!isNegotiable(t.perCapitaSpending)) {
                        et_per_apita_consumption.setText(t.perCapitaSpending)
                    }
                    if(!isNegotiable(t.deliveryFee)) {
                        et_shipping_fee.setText(t.deliveryFee)
                    }
                    tv_delivery_time.text = DbHelper.getInstance().getItemName(t.deliveryTime, C.ParentCode.DELIVERY_TIME)
                    params.deliveryTime = t.deliveryTime
                    tv_delivery_area.text = t.deliveryArea
                    params.deliveryArea = t.deliveryArea
                    et_description.setText(t.remark)
                }

                override fun onError(erroCode: Int, erroMsg: String) {
                    disMissLoadingView()
                    showToast(erroMsg)
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
                    disMissLoadingView()
                    showToast(msg)
                }
            })
    }

    companion object {
        const val SELECT_CITY = 1003
        const val SELECT_DELIVERY_AREA = 1004
        const val WORK_PERIOD = 1005
        const val SELECT_ADDRESS = 1006
        const val SELECT_PHOTO_REQUEST_CODE = 1009
        fun start(context: Context) {
            val starter = Intent(context, ReleaseShopActivity::class.java)
            context.startActivity(starter)
        }
        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleaseShopActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService:ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    val params = ReleaseLifeShopParams()

    lateinit var selectTimeDialog: SelectDateTimeDialog
    var isStartTime = true


    val androidImagePicker: AndroidImagePicker = AndroidImagePicker.getInstance()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_life_shop)
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                SELECT_CITY -> {
                    val city = data?.getStringExtra(C.IntentKey.CITY_NAME)
                    tv_city.text = city
                    params.city = city
                    MapPointActivity.start(this, SELECT_ADDRESS)
                }
                SELECT_ADDRESS -> {
                    val info = data?.getParcelableExtra<AddressInfo>(C.IntentKey.ADDRESS_INFO)
                    info?.apply {
                        et_shop_address.setText(addressName)
                        params.longitude = longitude
                        params.latitude = latitude
                    }
                }
                SELECT_DELIVERY_AREA -> {
                    val city = data?.getStringExtra(C.IntentKey.CITY_NAME)
                    tv_delivery_area.text = city
                    params.deliveryArea = city
                }
                WORK_PERIOD -> {
                    val list = data?.getParcelableArrayListExtra<ParentCodeInfo>(MultipleSelectionActivity.MULTIPLE_SELECTION_LIST)
                    var cycleName = ""
                    var code = ""
                    list?.forEach {
                        if(CommonUtil.isNotEmpty(cycleName)){
                            cycleName += "、"
                        }
                        if(CommonUtil.isNotEmpty(code)){
                            code += ","
                        }
                        cycleName += it.name
                        code += it.code
                    }
                    tv_business_cycle.text = cycleName
                    params.workPeriod = code
                }
                SELECT_PHOTO_REQUEST_CODE -> {
                    onPictureTakeComplete(data?.getStringExtra(SELECTED_PIC), requestCode)
                }
            }
        }
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        params.userOid = app.userOid

        selectTimeDialog = SelectDateTimeDialog(mContext, SelectDateTimeDialog.MODEL_TIME)
        selectTimeDialog.setOnSelectDateTimeListener { dateTime ->
            if(isStartTime){
                tv_opening_hours.text = dateTime
                params.workStartTimes = dateTime
            }else {
                tv_business_end_time.text = dateTime
                params.workEndTimes = dateTime
            }
        }

        tv_cuisine.clickWithTrigger {//菜系
            singleSelectPopWindow.apply {
                showPopupWindow(tv_cuisine, DbHelper.getInstance().getParentCodeList(C.ParentCode.COOK_STYLE))
                block = {
                    tv_cuisine.text = it.showName
                    params.cookStyle = it.showCode
                }
            }
        }
        tv_business_cycle.clickWithTrigger {
            MultipleSelectionActivity.start(this, "选择营业周期",
                    DbHelper.getInstance().getParentCodeList(C.ParentCode.WORK_PERIOD), WORK_PERIOD)
        }
        tv_delivery_time.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_delivery_time, DbHelper.getInstance().getParentCodeList(C.ParentCode.DELIVERY_TIME))
                block = {
                    tv_delivery_time.text = it.showName
                    params.deliveryTime = it.showCode
                }
            }
        }
        tv_city.clickWithTrigger {
            CityListActivity.start(this, SELECT_CITY)
        }
        tv_delivery_area.clickWithTrigger {
            CityListActivity.start(this, SELECT_DELIVERY_AREA)
        }

        tv_opening_hours.clickWithTrigger {
            isStartTime = true
            autoSize()
            selectTimeDialog.show()
        }
        tv_business_end_time.clickWithTrigger {
            isStartTime = false
            autoSize()
            selectTimeDialog.show()
        }

        tv_release.clickWithTrigger {
            if(isPictureDoesNotPass(0)){
                return@clickWithTrigger
            }
            params.title = et_shop_name.getStr()
            params.address = et_shop_address.getStr()
            params.startPrice = et_starting_price.getStr()
            params.perCapitaSpending = et_per_apita_consumption.getStr()
            params.deliveryFee = et_shipping_fee.getStr()
            params.remark = et_description.getStr()

            params.linkMan = et_contact.getStr()
            params.linkTel = et_phone.getStr()
            params.email = et_mailbox.getStr()
            params.qq = et_qq.getStr()
            params.weixin = et_wechat.getStr()

            if(params.isNotEmpty){
                processPhoto()
            }

        }

        iv_add_photo.clickWithTrigger {
            val intent = Intent()
            intent.setClass(mContext, ImagesGridActivity::class.java)
            startActivityForResult(intent, SELECT_PHOTO_REQUEST_CODE)
        }
        androidImagePicker.selectMode = AndroidImagePicker.Select_Mode.MODE_SINGLE
        androidImagePicker.isShouldShowCamera = true
        androidImagePicker.addOnPictureTakeCompleteListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        androidImagePicker.removeOnPictureTakeCompleteListener(this)
    }

    /**
     * 发布
     */
    override fun release(photoUrl: String) {
        params.mainPhoto = photoUrl
        params.oid = commonOid
        apiService.releaseShop(params)
            .compose(RxUtil.normalSchedulers<ResultBase<LifeFoodShopInfo>>())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeFoodShopInfo>>() {
                override fun onSuccess(result: ResultBase<LifeFoodShopInfo>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    finish()
                    EventNotifier.getInstance().updateLifeReleaseList()
                    showLifeDetailsWeb(LifeReleaseCommonUiModel.LIFE_REPAST_INFO, result.data.oid)
                }

                override fun onError(erroCode: Int, erroMsg: String) {
                    disMissLoadingView()
                    showToast(erroMsg)
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
                    disMissLoadingView()
                    showToast(msg)
                }
            })
    }




}