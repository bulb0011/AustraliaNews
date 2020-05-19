package com.ruanyun.australianews.ui.life.release

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.model.GoodsInfo
import com.ruanyun.australianews.model.params.ReleaseLifeShopGoodsParams
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.EventNotifier
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.activity_release_life_shop_goods.*
import javax.inject.Inject

/**
 * @description 发布餐饮美食信息
 * @author hdl
 * @date 2019/11/12
 */
class ReleaseShopGoodsActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getLifeShopGoodsInfo(commonOid)
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<GoodsInfo>>() {
                    override fun onSuccess(result: ResultBase<GoodsInfo>) {
                        disMissLoadingView()
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
        fun start(context: Context, shopOid: String?) {
            val starter = Intent(context, ReleaseShopGoodsActivity::class.java)
            starter.putExtra(C.IntentKey.SHOP_OID, shopOid)
            context.startActivity(starter)
        }

        fun startModify(context: Context, goodsOid: String) {
            val starter = Intent(context, ReleaseShopGoodsActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, goodsOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService: ApiService
    val params = ReleaseLifeShopGoodsParams()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_life_shop_goods)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        params.userOid = app.userOid
        params.infoRestaurantOid = intent.getStringExtra(C.IntentKey.SHOP_OID)

        tv_release.clickWithTrigger {
            if (isPictureDoesNotPass(1)) {
                return@clickWithTrigger
            }
            params.title = et_goods_name.getStr()
            params.price = et_price.getStr()
            params.description = et_description.getStr()
            if (params.isNotEmpty) {
                processPhoto()
            }

        }
    }


    /**
     * 发布
     */
    override fun release(photoUrl: String) {
        params.mainPhoto = photoUrl
        apiService.releaseShopGoods(params)
                .compose(RxUtil.normalSchedulers<ResultBase<*>>())
                .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                    override fun onSuccess(result: ResultBase<*>) {
                        disMissLoadingView()
                        showToast(result.msg)
                        finish()
                        EventNotifier.getInstance().updateShopGoodsPage()
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