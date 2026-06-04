package com.softage.net.vilcorp.ui.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.softage.net.vilcorp.R
import com.softage.net.vilcorp.adapter.MyListAdapter
import com.softage.net.vilcorp.databinding.ActivityMainBinding
import com.softage.net.vilcorp.network.ApiClient
import com.softage.net.vilcorp.network.ApiInterface
import com.softage.net.vilcorp.services.LocationService
import com.softage.net.vilcorp.ui.activity.onboarding.AttendanceActivity
import com.softage.net.vilcorp.ui.fragment.CancelOrderFragment
import com.softage.net.vilcorp.ui.fragment.HomeFragment
import com.softage.net.vilcorp.ui.fragment.OrderDetailsFragment
import com.softage.net.vilcorp.ui.fragment.ProfileFragment
import com.softage.net.vilcorp.util.NetworkHelper.isConnected
import com.softage.net.vilcorp.util.ToastHelper
import com.softage.net.vilcorp.util.Utility

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var binding: ActivityMainBinding?=null
    private var mUtil: Utility? = null
    private var mApiInterface: ApiInterface? = null
    var handler: Handler? = null
    lateinit var myListAdapter :MyListAdapter

    val icons = intArrayOf(
        R.drawable.baseline_drive_file_move_24, R.drawable.baseline_check_circle_24, R.drawable.baseline_cancel_24,
        R.drawable.baseline_group_24, R.drawable.baseline_restore_from_trash_24, R.drawable.baseline_help_24,
        R.drawable.baseline_mobile_screen_share_24, R.drawable.baseline_grade_24
    )

    val icon_name = arrayOf(
        "All Orders", "Delivered", "Cancelled", "Attendance", "Trash", "Help", "Share App", "Rate Us")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initBottomNav()
        mApiInterface = ApiClient.getRetrofit().create(ApiInterface::class.java)
        mUtil = Utility(this)

        binding!!.appBar.notifIcon.setOnClickListener {
            startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
        }

        //Drawer items
         myListAdapter = MyListAdapter(applicationContext, icons, icon_name)
        binding!!.navigationLayout.navListView.setAdapter(myListAdapter)

        binding!!.appBar.menuIcon.setOnClickListener {

            binding!!.drawerLayout.openDrawer(Gravity.LEFT);
        }
        binding!!.navigationLayout.navListView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            myListAdapter.setSelectedPosition(i)

            when (i) {
                0 -> {
//                    if (isConnected(applicationContext)) {
//                        val intent = Intent(
//                            this@MainActivity,
//                            AllFilesActivity::class.java
//                        )
//                        startActivity(intent)
//                    } else {
//                        ToastHelper().snackBar(
//                            findViewById<View>(android.R.id.content),
//                            getResources().getString(com.softage.net.softdocs.R.string.no_network)
//                        )
//                    }
                    binding!!.drawerLayout.close()
                }

                1 -> {

                    if (isConnected(applicationContext)) {
                        val intent = Intent(
                            this@MainActivity,
                            DeliveredActivity::class.java
                        )
                        startActivity(intent)
                    } else {
                        ToastHelper().snackBar(
                            findViewById<View>(android.R.id.content),
                            getResources().getString(com.softage.net.vilcorp.R.string.no_network)
                        )
                    }
                    binding!!.drawerLayout.close()
                }

                2 -> {
//
                    if (isConnected(applicationContext)) {
                        val intent = Intent(
                            this@MainActivity,
                            CancelledOrderActivity::class.java
                        )
                        startActivity(intent)
                    } else {
                        ToastHelper().snackBar(
                            findViewById<View>(android.R.id.content),
                            getResources().getString(com.softage.net.vilcorp.R.string.no_network)
                        )
                    }
                    binding!!.drawerLayout.close()
                }

                3 -> {
                    if (isConnected(applicationContext)) {
                        val intent = Intent(
                            this@MainActivity,
                            AttendanceActivity::class.java
                        )
                        startActivity(intent)
                    } else {
                        ToastHelper().snackBar(
                            findViewById<View>(android.R.id.content),
                            getResources().getString(com.softage.net.vilcorp.R.string.no_network)
                        )
                    }
                    binding!!.drawerLayout.close()
                }

                4 -> {
//                    if (isConnected(applicationContext)) {
//                        val intent = Intent(
//                            this@MainActivity,
//                            TrashActivity::class.java
//                        )
//                        startActivity(intent)
//                    } else {
//                        ToastHelper().snackBar(
//                            findViewById<View>(android.R.id.content),
//                            getResources().getString(com.softage.net.softdocs.R.string.no_network)
//                        )
//                    }
                    binding!!.drawerLayout.close()
                }

                5 -> {
//                    if (isConnected(applicationContext)) {
//                        val intent = Intent(
//                            this@MainActivity,
//                            ContactUsActivity::class.java
//                        )
//                        startActivity(intent)
//                    } else {
//                        ToastHelper().snackBar(
//                            findViewById<View>(android.R.id.content),
//                            getResources().getString(com.softage.net.softdocs.R.string.no_network)
//                        )
//                    }
                    binding!!.drawerLayout.close()
                }

                6 -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.setType("text/plain")
                    val shareBody =
                        "CheckOut SoftDocs App - \nhttps://play.google.com/store/apps/details?id=$packageName"
                    intent.putExtra(Intent.EXTRA_SUBJECT, "CheckOut SoftDocs app")
                    intent.putExtra(Intent.EXTRA_TEXT, shareBody)
                    startActivity(Intent.createChooser(intent, "Share using..."))
                    binding!!.drawerLayout.close()
                }

                7 -> {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$packageName")
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                            )
                        )
                    }
                    binding!!.drawerLayout.close()
                }

            }
        })
        //Drawer items end
        binding!!.appBar.tvName.text = "Hi, Ankit Kumar"
        binding!!.navigationLayout.linearLayout.setOnClickListener {
            mUtil!!.clear()
            finish()
        }

    }
    private fun initBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(this)
        loadFragment(HomeFragment())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        val id = item.itemId
        when (id) {
            R.id.nav_home -> {
                fragment = HomeFragment()
            }
            R.id.nav_fav -> {
                fragment = HomeFragment()
            }
            R.id.nav_Storage -> {
                fragment = HomeFragment()
            }
            R.id.nav_profile -> {
                fragment = ProfileFragment()
            }
        }
        return loadFragment(fragment)
    }

    override fun onResume() {
        super.onResume()
        myListAdapter.setSelectedPosition(-1)
        val intent = Intent(this@MainActivity, LocationService::class.java)
        ContextCompat.startForegroundService(this@MainActivity, intent)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, it)
                .commit()
            return true
        }
        return false
    }
}