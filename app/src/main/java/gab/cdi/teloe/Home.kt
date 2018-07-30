package gab.cdi.teloe

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.teleo_drawer.view.*

class Home : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, TeleoFragment {
    private var backToggle = 1
    private lateinit var teleoDrawer : LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        teleoDrawer = findViewById(R.id.teleoDrawer)
        initializeUserInDrawer()
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        displaySelectedScreen(R.id.nav_home)
    }

    private fun initializeUserInDrawer(){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if(userId != null){
            val userReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
            userReference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot?) {
                    val fetchedUser = p0?.getValue(User::class.java)
                    teleoDrawer.userFullName.text = fetchedUser?.fullname
                    teleoDrawer.userEmail.text = fetchedUser?.email
                }
                override fun onCancelled(p0: DatabaseError?) {
                }
            })
        }
    }
    override fun onBackPressed() {

        val mFragment = fragmentManager.findFragmentById(R.id.content_home)
        if(mFragment is WhatsOn){
            displaySelectedScreen(R.id.nav_home)
            return
        }

        if(mFragment is ShowDrilldown){
            displaySelectedScreen(R.id.nav_shows_microsite)
            return
        }

        if(mFragment is ShowsMicrosite){
            displaySelectedScreen(R.id.nav_microsites)
            return
        }

        if(mFragment !is HomeFragment && mFragment !is MandeleiTV){
            displaySelectedScreen(R.id.nav_home)
            return
        }

        if (drawer_layout.isDrawerOpen(GravityCompat.START) && backToggle == 0) {
            finish()
        }
        else if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
            backToggle = 1
        }

        if(!drawer_layout.isDrawerOpen(GravityCompat.START)){
            backToggle = 0
            drawer_layout.openDrawer(GravityCompat.START)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> logout()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
       /* when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }*/
        val id : Int = item.itemId
        displaySelectedScreen(id)
        return true
    }

    override fun onClick(view : View){
        val id : Int = view.id
        displaySelectedScreen(id)
    }


    private fun displaySelectedScreen(id : Int) {
        var fragment : Fragment? = null
        val mFragment = fragmentManager.findFragmentById(R.id.content_home)
        when (id) {
            R.id.nav_home -> {
                if(mFragment is HomeFragment){
                    drawer_layout.closeDrawer(GravityCompat.START)
                    return
                }
                fragment = HomeFragment.newInstance("","")
            }

            R.id.nav_trending -> {
                if(mFragment is TrendingNow){
                    drawer_layout.closeDrawer(GravityCompat.START)
                    return
                }
                fragment = TrendingNow.newInstance("","")
            }

            R.id.nav_schedule -> {
                if(mFragment is ScheduleFragment){
                    drawer_layout.closeDrawer(GravityCompat.START)
                    return
                }
                fragment = ScheduleFragment.newInstance("","")
            }

            R.id.nav_region -> {
                if(mFragment is RegionFragment){
                    drawer_layout.closeDrawer(GravityCompat.START)
                    return
                }
                fragment = RegionFragment.newInstance("","")
            }

            R.id.nav_history -> {
                if(mFragment is HistoryFragment){
                    drawer_layout.closeDrawer(GravityCompat.START)
                    return
                }
                fragment = HistoryFragment.newInstance("","")
            }

            R.id.nav_notification ->  {
                if(mFragment is NotificationFragment){
                    drawer_layout.closeDrawer(GravityCompat.START)
                    return
                }
                fragment = NotificationFragment.newInstance("","")
            }

            R.id.nav_microsites -> {
                if(mFragment is AboutMicrosite){
                    drawer_layout.closeDrawer(GravityCompat.START)
                    return
                }
                fragment = AboutMicrosite.newInstance("","")
            }

            R.id.nav_shows_microsite -> {
                fragment = ShowsMicrosite.newInstance("","")
            }

            R.id.nav_skins -> {
                if(mFragment is Skins){
                    drawer_layout.closeDrawer(GravityCompat.START)
                    return
                }
                fragment = Skins.newInstance("","")
            }

            R.id.nav_mandeleitv -> {
                val userSettings = getSharedPreferences("User Settings",0)
                val theme = userSettings.getInt("themeUsed",0)
                when (theme) {
                    0 -> fragment = MandeleiTV.newInstance("classic","")
                    1 -> fragment = MandeleiTV.newInstance("retro","")
                    2 -> fragment = MandeleiTV.newInstance("modern","")

                }

            }

            R.id.viewing_skin_1940 -> {
                fragment = MandeleiTV.newInstance("1940","")
            }

            R.id.viewing_skin_1950 -> {
                fragment =  MandeleiTV.newInstance("1950","")
            }

            R.id.viewing_skin_1960 -> {
                fragment =  MandeleiTV.newInstance("1960","")
            }
            R.id.viewing_skin_1970 -> {
                fragment =  MandeleiTV.newInstance("1970","")
            }

            R.id.viewing_skin_1980 -> {
                fragment =  MandeleiTV.newInstance("1980","")
            }

            R.id.viewing_skin_1990 -> {
                fragment =  MandeleiTV.newInstance("1990","")
            }
            R.id.viewing_skin_modern -> {
                fragment =  MandeleiTV.newInstance("modern","")
            }
        }

        if (fragment != null){
            val ft = fragmentManager.beginTransaction()
            ft.replace(R.id.content_home,fragment)
            ft.commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)

    }

    override fun onFragmentInteraction(uri : Uri){

    }

    private fun logout() : Boolean{
        val logoutAuth = FirebaseAuth.getInstance()
        logoutAuth.signOut()
        val userSettings : SharedPreferences.Editor = getSharedPreferences("User Settings", Context.MODE_PRIVATE).edit()
        userSettings.clear()
        userSettings.apply()
        val signInIntent = Intent(applicationContext,SignIn::class.java)
        startActivity(signInIntent)

        finish()
        return true
    }



}
