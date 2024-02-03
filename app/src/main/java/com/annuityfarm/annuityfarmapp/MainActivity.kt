package com.annuityfarm.annuityfarmapp

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.annuityfarm.annuityfarmapp.broadcastreceivers.ConnectivityReceiver
import com.annuityfarm.annuityfarmapp.databinding.*
import com.annuityfarm.annuityfarmapp.ext.alert
import com.annuityfarm.annuityfarmapp.ext.goToActivity
import com.annuityfarm.annuityfarmapp.ext.goToFragment
import com.annuityfarm.annuityfarmapp.ext.lazyFast
import com.annuityfarm.annuityfarmapp.libraries.documentscanner.MenuActivity
import com.microblink.blinkinput.entities.recognizers.Recognizer
import com.microblink.blinkinput.entities.recognizers.RecognizerBundle
import com.microblink.blinkinput.uisettings.ActivityRunner


class MainActivity : AppCompatActivity() ,ConnectivityReceiver.ConnectivityReceiverListener{

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        ///findViewById(R.id.nav_host_fragment_content_main)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        setupSmoothBottomMenu()

        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

       topRightMenus()

    }

    private fun topRightMenus(){
        binding.btnNotification.setOnClickListener {
            binding.toolbarTitle.text = "Notifications"
            binding.imgAnnuityLogo.gone()
            binding.btnScan.gone()
            navController.navigate(R.id.action_home_fragment_to_NotificationFragment)
        }
    }


    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu_bottom)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController, binding.toolbarTitle, binding.imgAnnuityLogo)

    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    //////////////////////////////////////////INTERNET CONNECTION////////////////////////////////////////////////////
    private val alert: AlertDialog by lazyFast {
         alert(
            cancelable = false,
            showIcon = R.drawable.ic_wi_fi_off,
            "Internet Connection Alert",
            "Please check your internet connection"
        ) {
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
//          snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            alert.show()
        } else {
            alert.dismiss()
        }
    }

    ///////////////////////////////////////////BLINK ID//////////////////////////////////////////////////


}