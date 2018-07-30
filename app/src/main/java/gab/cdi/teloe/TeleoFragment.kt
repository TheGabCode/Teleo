package gab.cdi.teloe

/**
 * Created by Default on 10/07/2018.
 */
interface TeleoFragment : HomeFragment.OnFragmentInteractionListener,
        WhatsOn.OnFragmentInteractionListener,
        TrendingNow.OnFragmentInteractionListener,
        ScheduleFragment.OnFragmentInteractionListener,
        RegionFragment.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener,
        HistoryFragment.OnFragmentInteractionListener,
        AboutMicrosite.OnFragmentInteractionListener,
        Skins.OnFragmentInteractionListener,
        ShowsMicrosite.OnFragmentInteractionListener,
        ShowDrilldown.OnFragmentInteractionListener,
        MandeleiTV.OnFragmentInteractionListener{
}