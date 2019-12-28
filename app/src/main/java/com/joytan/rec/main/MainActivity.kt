package com.joytan.rec.main

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log

import com.joytan.util.BWU
import com.joytan.rec.R

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import com.google.android.gms.appinvite.AppInviteInvitation
import com.joytan.rec.setting.HtmlViewerActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




/**
 * Main screen
 */
class MainActivity : AppCompatActivity() {

    companion object {
        val CODE_SETTING = 1
        val INFO_TAG = "print_debug"
        /**
         * Show progress of data transaction with Firebase server
         */
        lateinit var pd: ProgressDialog
    }

    /**
     * Toolbar and NavDrawer configuration
     */
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    // Internet connection receiver/monitor
    private val connFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
        addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
    }
    private val connReceiver = ConnectionReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver(connReceiver, connFilter)

        // Get permission first before making myDones into the user's storage
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        10)
            }
        } else {
            // Permission has already been granted
        }
        BWU.log("MainActivity#onCreate")
        // Title in toolbar is empty by default
        // and to be changed to project name in the MainFragment
        toolbar_main.title = ""
        setSupportActionBar(toolbar_main)
        // Configure ad
        // setupAd()
        // Volume adjusted to music

        // DO NOT ALLOW USERS TO TOUCH BUTTON BEFORE LOADING CONTENT
        pd = ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT)
        // TODO Synchronize with the actual internet connection time
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.setMessage("Loading data from server ...")
        pd.setCancelable(false)
        pd.show()

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
                setOf(R.id.nav_main), drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
//        nav_view.itemIconTintList = null
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_slack -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://slack-invite.joytan.pub/")
                    startActivity(intent)
                }
                R.id.nav_pub -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://joytan.pub")
                    startActivity(intent)
                }
                R.id.nav_youtube -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://youtube.com/c/JoytanApp")
                    startActivity(intent)
                }
                R.id.nav_share -> {
                    val intent = AppInviteInvitation.IntentBuilder(getString(R.string.invite_title))
                            .setDeepLink(Uri.parse("https://cp999.app.goo.gl/cffF"))
                            .build();
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivityForResult(intent,1);
                }
                else -> {
                    navController.navigate(it.itemId)
                }
            }
            drawer_layout.closeDrawers()
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun lockDrawer() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun unlockDrawer() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}
