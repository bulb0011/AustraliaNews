package com.ruanyun.australianews.ui.life.release

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.ext.clickWithTrigger
import kotlinx.android.synthetic.main.activity_release_life_pet_transaction_select_type.*

/**
 * @description 发布宠物交易选择类型
 * @author hdl
 * @date 2019/5/28
 */
class ReleasePetTransactionSelectTypeActivity : BaseActivity() {

    companion object {
        const val REQUEST_CODE_RELEASE_SUCCESS = 1006
        const val ALL_PET_TYPE_CAT = "ALL_PET_TYPE_CAT"
        const val ALL_PET_TYPE_DOG = "ALL_PET_TYPE_DOG"
        const val ALL_PET_TYPE_FISH = "ALL_PET_TYPE_FISH"
        const val ALL_PET_TYPE_PET_SUPPLIES = "ALL_PET_TYPE_CWYP"
        fun start(context: Context) {
            val starter = Intent(context, ReleasePetTransactionSelectTypeActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_RELEASE_SUCCESS) {
            finish()
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_life_pet_transaction_select_type)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)

        tv_pet_cat.clickWithTrigger {
            ReleasePetTransactionActivity.start(this, ALL_PET_TYPE_CAT, REQUEST_CODE_RELEASE_SUCCESS)
        }
        tv_pet_dog.clickWithTrigger {
            ReleasePetTransactionActivity.start(this, ALL_PET_TYPE_DOG, REQUEST_CODE_RELEASE_SUCCESS)
        }
        tv_flower_bird_fish_insect.clickWithTrigger {
            ReleasePetTransactionActivity.start(this, ALL_PET_TYPE_FISH, REQUEST_CODE_RELEASE_SUCCESS)
        }
        tv_pet_supplies.clickWithTrigger {
            ReleasePetTransactionActivity.start(this, ALL_PET_TYPE_PET_SUPPLIES, REQUEST_CODE_RELEASE_SUCCESS)
        }
    }


}