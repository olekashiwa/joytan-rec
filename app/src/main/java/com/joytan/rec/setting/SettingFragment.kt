package com.joytan.rec.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import com.google.android.gms.appinvite.AppInviteInvitation

import com.joytan.rec.R


/**
 * 設定画面のフラグメント
 */
class SettingFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.setting)
        run {
            //紹介
            val pref = findPreference(PREF_INVITE)
            pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callInviteActivity()
                true
            }
        }
        run {
            //商品情報
            val pref = findPreference(PREF_ABOUT)
            pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callAboutActivity()
                true
            }
        }
        run {
            //プライバシーポリシー
            val pref = findPreference(PREF_PP)
            pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callPP()
                true
            }
        }
        run {
            val pref = findPreference(PREF_TW)
            pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callTwitter()
                true
            }
        }
        run {
            val pref = findPreference(PREF_YT)
            pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callYouTube()
                true
            }
        }
        run {
            val pref = findPreference(PREF_WEB)
            pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                callWebsite()
                true
            }
        }
        run {
            val pref = findPreference(PREF_CRED)

            // Initialize summary text
            val manager = PreferenceManager.getDefaultSharedPreferences(pref.context)
            pref.summary = manager.getString(PREF_CRED, null)

            pref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener {
                preference: Preference, value: Any ->
                pref.summary = value.toString()
                true
            }
        }
    }

    private fun callInviteActivity() {
        //ソースコード公開用に招待は無効
        val intent = AppInviteInvitation.IntentBuilder(getString(R.string.pref_invite))
                .setDeepLink(Uri.parse("https://cp999.app.goo.gl/cffF"))
                .build();
        startActivityForResult(intent,1);
    }

    private fun callAboutActivity() {
        val intent = Intent(activity, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun callOSS() {
        val intent = Intent(activity, HtmlViewerActivity::class.java)
        intent.putExtra(HtmlViewerActivity.EXTRA_URL, "file:///android_asset/license.txt")
        startActivity(intent)
    }

    private fun callPP() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://quick-echo.firebaseapp.com/")
        startActivity(intent)
    }
    private fun callTwitter() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://twitter.com/JoytanApp")
        startActivity(intent)
    }
    private fun callYouTube() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.youtube.com/c/JoytanApp")
        startActivity(intent)
    }

    private fun callWebsite() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://kokimame.github.io/joytan/")
        startActivity(intent)
    }

    companion object {

        private val PREF_SOUND_EFFECT = "soundEffect"

        private val PREF_ABOUT = "about"

        private val PREF_INVITE = "invite"

        private val PREF_OSS = "oss"

        private val PREF_PAGE = "page"

        private val PREF_PP = "pp"

        private val PREF_TW = "twitter"

        private val PREF_YT = "youtube"

        private val PREF_WEB = "website"

        private val PREF_CRED = "creditText"


        /**
         * 効果音設定を取得する
         * @param context
         * @return
         */
        fun isSoundEffect(context: Context): Boolean {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return pref.getBoolean(PREF_SOUND_EFFECT, true)
        }
        fun getCredit(context: Context): String? {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return pref.getString(PREF_CRED, null)
        }
    }
}