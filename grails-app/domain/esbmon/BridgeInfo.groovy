package esbmon

class BridgeInfo {

    // Bridge Information
    boolean createdByDuplex = false
    long dequeueCounter = 0
    long enqueueCounter = 0

    String remoteAddress = "no data"
    String remoteBrokerName = "no data"

    def NetworkInfo networkInfo
    static belongsTo = [NetworkInfo]

    static constraints = {
    }
}
