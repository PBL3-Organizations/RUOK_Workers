package com.example.ruok_workersimport BriefingBoardFragmentimport android.content.Intentimport android.os.Bundleimport android.util.Logimport android.view.MenuItemimport androidx.appcompat.app.ActionBarDrawerToggleimport androidx.appcompat.app.AppCompatActivityimport androidx.fragment.app.Fragmentimport com.example.ruok_workers.databinding.ActivityDashboardBindingclass DashboardActivity : AppCompatActivity() {    private lateinit var toggle: ActionBarDrawerToggle    private lateinit var binding: ActivityDashboardBinding    private lateinit var intent: Intent    private var loginNum: Int = -1    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_dashboard)        if (savedInstanceState == null) {            setFragment(DashboardFragment())        }        //로그인 회원 정보        intent = getIntent()        loginNum = intent.getIntExtra("m_num", 0)        //네비게이션 드로어 바인딩        binding = ActivityDashboardBinding.inflate(layoutInflater)        setContentView(binding.root)        supportActionBar?.setDisplayHomeAsUpEnabled(true)        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.app_name, R.string.app_name)        toggle.syncState()        setFragment(DashboardFragment())        //네비게이션 드로어 선택 시 해당 Fragment로 전환        binding.navigationView.setNavigationItemSelectedListener { item ->            when (item.itemId) {                R.id.tabLogout -> setFragment(LogoutFragment())                R.id.tabInfoRevision -> setFragment(CheckPwFragment())                R.id.tabDashboard -> setFragment(DashboardFragment())                R.id.tabList -> setFragment(ListFragment())                R.id.tabCountingList -> setFragment(CountingListFragment())                R.id.tabBriefingBoard -> setFragment(BriefingBoardFragment())                R.id.tabUnknownHomeless -> setFragment(UnknownHomelessFragment())                R.id.tabHomelessList -> setFragment(SearchFragment())            }            binding.drawerLayout.closeDrawers()            false        }    }    //나중에 값 전달시 용이하게 함수로 작성함    fun setFragment(fragment: Fragment){        var bundle = Bundle()        //로그인 정보 전달        bundle.putInt("m_num", loginNum)        //상담내역 번호 전달        val c_num = fragment.arguments?.getInt("c_num", 0)        if (c_num != null) {            bundle.putInt("c_num", c_num)            val h_num = fragment.arguments?.getInt("h_num", -1)            if (h_num != null) bundle.putInt("h_num", h_num)        }        //아웃리치 진행 중인 경우        val onRecording = fragment.arguments?.getInt("onRecording", 0)        if (onRecording != null) bundle.putInt("onRecording", onRecording)        //상담내역 작성 중인 경우        val hasConsultation = fragment.arguments?.getInt("hasConsultation", 0)        if (hasConsultation == 1) {            val item = fragment.arguments?.getParcelable<ConsultationItem>("consultation_item")            bundle.putInt("hasConsultation", hasConsultation)            bundle.putParcelable("consultation_item", item)        }        fragment.arguments = bundle        supportFragmentManager.beginTransaction().replace(R.id.rootLayout, fragment).addToBackStack(null).commit()    }    override fun onOptionsItemSelected(item: MenuItem): Boolean {        if (toggle.onOptionsItemSelected(item)) {            return true        }        return super.onOptionsItemSelected(item)    }}