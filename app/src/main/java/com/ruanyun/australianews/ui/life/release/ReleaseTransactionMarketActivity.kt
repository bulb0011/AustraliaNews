package com.ruanyun.australianews.ui.life.release

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.impl.RvCommonAdapter
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.model.LifeCarSaleInfo
import com.ruanyun.australianews.model.LifeTransactionMarketInfo
import com.ruanyun.australianews.model.ParentCodeInfo
import com.ruanyun.australianews.model.params.ReleaseLifeCarSaleParams
import com.ruanyun.australianews.model.params.ReleaseLifeTransactionMarketParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.LifeTransactionMarketFilterPopWindow
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_release_life_transaction_market.*
import kotlinx.android.synthetic.main.layout_life_release_contact.*
import kotlinx.android.synthetic.main.layout_life_release_details_fill_in.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import javax.inject.Inject

/**
 * @description 发布交易市场
 * @author hdl
 * @date 2019/5/25
 */
class ReleaseTransactionMarketActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getLifeTransactionMarketInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeTransactionMarketInfo>>() {
                override fun onSuccess(result: ResultBase<LifeTransactionMarketInfo>) {
                    disMissLoadingView()
                    val t = result.data
                    setReleasePhoto(t.mainPhoto, t.title)
//                    setReleaseDetails(t.descriptionTag)
                    setReleaseContact(t.linkMan, t.linkTel, t.email, t.qq, t.weixin)
                    tv_identity.text = DbHelper.getInstance().getItemNames(t.dignity, C.ParentCode.HOUSING_SOURCE)
                    params.dignity = t.dignity
                    tv_transaction_type.text = DbHelper.getInstance().getItemNames(t.transactionType, C.ParentCode.MEANS_TRANSACTION)
                    params.transactionType = t.transactionType
                    tv_product_categories.text = DbHelper.getInstance().getItemNames(t.type, C.ParentCode.PRODUCT_CATEGORIES)
                    params.type = t.type
                    tv_old_and_new.text = DbHelper.getInstance().getItemNames(t.newOldStandard, C.ParentCode.NEWOID_STANDARD)
                    params.newOldStandard = t.newOldStandard
                    tv_under_warranty.text = DbHelper.getInstance().getItemNames(t.warranty, C.ParentCode.WARRANTY)
                    params.warranty = t.warranty
                    tv_nature_transaction.text = DbHelper.getInstance().getItemNames(t.transactionNature, C.ParentCode.TRANSACTION_NATURE)
                    params.transactionNature = t.transactionNature
                    tv_trading_area.text = DbHelper.getInstance().getItemNames(t.transactionArea, C.ParentCode.TRANSACTION_AREA)
                    params.transactionArea = t.transactionArea

                    tv_city.text = t.city
                    params.city = t.city
                    if(!isNegotiable(t.price)) {
                        et_price.setText(t.price)
                    }
                    if(!isNegotiable(t.deliveryPrice)) {
                        et_delivery_fee.setText(t.deliveryPrice)
                    }
                    et_product_description.setText(t.description)

                    adapter.selectList.clear()
                    val tagList = DbHelper.getInstance().getParentCodeList(C.ParentCode.TMARKETDESCRIPTION)
                    tagList.forEach {
                        if(t.descriptionTag?.contains(it.code) == true){
                            it.isSelect = true
                            adapter.selectList.add(it.code)
                        }else {
                            it.isSelect = false
                        }
                    }
                    adapter.refresh(tagList)
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
        fun start(context: Context) {
            val starter = Intent(context, ReleaseTransactionMarketActivity::class.java)
            context.startActivity(starter)
        }
        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleaseTransactionMarketActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService:ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    val params = ReleaseLifeTransactionMarketParams()
    val adapter by lazy { LabelListAdapter(mContext, R.layout.item_list_transaction_market_label, arrayListOf()) }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_life_transaction_market)
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
                }
            }
        }
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        setInputDetailsName("附注信息")
        params.userOid = app.userOid

        adapter.setData(DbHelper.getInstance().getParentCodeList(C.ParentCode.TMARKETDESCRIPTION))
        rv_label.adapter = adapter
        rv_label.layoutManager = GridLayoutManager(mContext, 4)

        tv_city.clickWithTrigger {
            CityListActivity.start(this, SELECT_CITY)
        }
        tv_identity.clickWithTrigger {//身份
            singleSelectPopWindow.apply {
                showPopupWindow(tv_identity, DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSING_SOURCE))
                block = {
                    tv_identity.text = it.showName
                    params.dignity = it.showCode
                }
            }
        }
        tv_transaction_type.clickWithTrigger {//交易类型
            singleSelectPopWindow.apply {
                showPopupWindow(tv_transaction_type, DbHelper.getInstance().getParentCodeList(C.ParentCode.MEANS_TRANSACTION))
                block = {
                    tv_transaction_type.text = it.showName
                    params.transactionType = it.showCode
                }
            }
        }
        tv_product_categories.clickWithTrigger {//产品分类
            singleSelectPopWindow.apply {
                showPopupWindow(tv_product_categories, DbHelper.getInstance().getParentCodeList(C.ParentCode.PRODUCT_CATEGORIES))
                block = {
                    tv_product_categories.text = it.showName
                    params.type = it.showCode
                }
            }
        }
        tv_old_and_new.clickWithTrigger {//新旧程度
            singleSelectPopWindow.apply {
                showPopupWindow(tv_old_and_new, DbHelper.getInstance().getParentCodeList(C.ParentCode.NEWOID_STANDARD))
                block = {
                    tv_old_and_new.text = it.showName
                    params.newOldStandard = it.showCode
                }
            }
        }
        tv_under_warranty.clickWithTrigger {//保修期内
            singleSelectPopWindow.apply {
                showPopupWindow(tv_under_warranty, DbHelper.getInstance().getParentCodeList(C.ParentCode.WARRANTY))
                block = {
                    tv_under_warranty.text = it.showName
                    params.warranty = it.showCode
                }
            }
        }
        tv_nature_transaction.clickWithTrigger {//交易方式
            singleSelectPopWindow.apply {
                showPopupWindow(tv_nature_transaction, DbHelper.getInstance().getParentCodeList(C.ParentCode.TRANSACTION_NATURE))
                block = {
                    tv_nature_transaction.text = it.showName
                    params.transactionNature = it.showCode
                }
            }
        }
        tv_trading_area.clickWithTrigger {//交易区域
            singleSelectPopWindow.apply {
                showPopupWindow(tv_trading_area, DbHelper.getInstance().getParentCodeList(C.ParentCode.TRANSACTION_AREA))
                block = {
                    tv_trading_area.text = it.showName
                    params.transactionArea = it.showCode
                }
            }
        }

        tv_release.clickWithTrigger {
            if(isPictureDoesNotPass(1)){
                return@clickWithTrigger
            }
            params.title = et_title.getStr()
            params.price = et_price.getStr()
            params.deliveryPrice = et_delivery_fee.getStr()
            params.description = et_product_description.getStr()
            params.descriptionTag = adapter.getSelectCodes()

            params.linkMan = et_contact.getStr()
            params.linkTel = et_phone.getStr()
            params.email = et_mailbox.getStr()
            params.qq = et_qq.getStr()
            params.weixin = et_wechat.getStr()

            if(params.isNotEmpty){
                processPhoto()
            }

        }
    }



    /**
     * 发布
     */
    override fun release(photoUrl: String) {
        params.mainPhoto = photoUrl
        params.oid = commonOid
        apiService.releaseTransactionMarket(params)
            .compose(RxUtil.normalSchedulers<ResultBase<LifeCarSaleInfo>>())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeCarSaleInfo>>() {
                override fun onSuccess(result: ResultBase<LifeCarSaleInfo>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    finish()
                    EventNotifier.getInstance().updateLifeReleaseList()
                    showLifeDetailsWeb(LifeReleaseCommonUiModel.LIFE_TRANSACTION_MARKET_INFO, result.data.oid)
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


    class LabelListAdapter(context: Context?, layoutId: Int, datas: MutableList<ParentCodeInfo>?) : RvCommonAdapter<ParentCodeInfo>(context, layoutId, datas) {

        val selectList = arrayListOf<String>()

        override fun convert(holder: ViewHolder?, item: ParentCodeInfo, position: Int) {
            val tvTitle = holder?.getView<TextView>(R.id.tv_title)
            tvTitle?.isSelected = item.isSelect
            tvTitle?.text = item.name
            holder?.convertView?.click {
                if(item.isSelect){
                    item.isSelect = false
                    tvTitle?.isSelected = item.isSelect
                    selectList.remove(item.code)
                }else {
//                    if(selectList.size>=3){
//                        CommonUtil.showToast("最多选择三项")
//                    }else {
                        item.isSelect = true
                        tvTitle?.isSelected = item.isSelect
                        selectList.add(item.code)
//                    }
                }
            }
        }

        fun getSelectCodes(): String{
            return if(selectList.isNotEmpty()){
                var codes = ""
                selectList.forEach {
                    if(TextUtils.isEmpty(codes)){
                        codes = it
                    }else {
                        codes += ",$it"
                    }
                }
                codes
            }else {
                ""
            }
        }

    }

}