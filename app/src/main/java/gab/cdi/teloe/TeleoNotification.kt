package gab.cdi.teloe

/**
 * Created by Default on 23/07/2018.
 */
data class TeleoNotification(var programId : String = "",
                        var eventType : Int = 0,
                        var source : String = "",
                        var recipient : String = ""){

}