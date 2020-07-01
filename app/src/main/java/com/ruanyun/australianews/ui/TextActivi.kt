package com.ruanyun.australianews.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.iflytek.cloud.SpeechSynthesizer
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import kotlinx.android.synthetic.main.activitty_text.*

class TextActivi : BaseActivity() {


    var text="Hooray! It's snowing! It's time to make a sn" +
            "owman.James runs out. He makes a big pile of s" +
            "now. He puts a big snowball on top. He adds a scarf and a hat. He adds an orange for the nose. He adds coal for the eyes and buttons.In the evening, James opens the door. What does he see? The snowman is moving! James invites him in. The snowman has never been inside a house. He says hello to the cat. He plays with paper towels.A moment later, the snowman takes James's hand and goes out.They go up, up, up into the air! They are flying! What a wonderful night!The next morning, James jumps out of bed. He runs to the door.He wants to thank the snowman. But he's gone.门。他看见了什么？雪人在移动！詹姆斯邀请它进来。雪人从来没有去过房间里面。它对猫咪打了个招呼。猫咪玩着纸巾。不久之后，雪人牵着詹姆斯的手出去了。他们一直向上升，一直升到空中！他们在飞翔！多么美妙的夜晚！第二天早上，詹姆斯从床上蹦了起来。他向门口跑去。他想感谢雪人，但是它已经消失了。"


    lateinit var mTts: SpeechSynthesizer

    lateinit var ttsholder : TtsHolder

    lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        setContentView(R.layout.activitty_text)

        ttsholder=TtsHolder(this,text)

        kaihsi.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View?) {

                ttsholder.start();

            }
        })


        zanting.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View?) {

                ttsholder.pauseSpeaking();

            }
        })


        jixu.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View?) {

                ttsholder.resumeSpeaking();

            }
        })


        ttsholder.setOnSpeakProgress(object : TtsHolder.OnSpeakProgress{
            override fun onSpeakProgress(percent: Int) {
                name.setText(percent.toString())

            }
        })


    }


    override fun onDestroy() {
        super.onDestroy()
        ttsholder.stopSpeaking()
    }




}