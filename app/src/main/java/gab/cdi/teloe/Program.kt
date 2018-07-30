package gab.cdi.teloe

data class Program (var programName : String = "",
                    var programThumbnailUrl : String? = null,
                    var programDescription : String = "",
                    var programAirTime : String = "",
                    var programNumber : Int? = null, //can be null, possible use case: see Trending Now mockup screen
                    var daysAired : HashMap<String,Boolean> = HashMap(),
                    var programId : String = ""){



    constructor(programName : String, programThumbnailUrl : String, programDescription : String, programAirTime : String, programNumber : Int, daysAired : HashMap<String,Boolean>, id : String) : this() {
        this.programName = programName
        this.programThumbnailUrl = programThumbnailUrl
        this.programDescription = programDescription
        this.programAirTime = programAirTime
        this.programNumber = programNumber
        this.daysAired = daysAired
        this.programId = id
    }

    constructor(programName : String, programThumbnailUrl : String, programDescription : String, programAirTime : String, programNumber : Int, id : String) : this() {
        this.programName = programName
        this.programThumbnailUrl = programThumbnailUrl
        this.programDescription = programDescription
        this.programAirTime = programAirTime
        this.programNumber = programNumber
        this.daysAired = HashMap()
        this.programId = id
    }


}