package esbmon

class NetworkInfo {

    def SampleTime sampleTime
    def Broker broker

    // NetworkConnection Fields
    String name

    boolean bridgeTempDestination = false
    boolean conduitSubscriptions = false
    boolean decreaseNetworkConsumerPriority = false
    boolean dispatchAsync = false
    boolean duplex = false
    boolean dynamicOnly = true

    int networkTtl = 0
    int prefetchSize = 0

    static belongsTo = [SampleTime, Broker]
    static hasMany = [bridges: BridgeInfo]
    static constraints = {
    }
}
