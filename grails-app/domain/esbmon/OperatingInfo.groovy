package esbmon

class OperatingInfo {

  def SampleTime sampleTime
  def Broker broker

  // memory info
  Long usedHeap
  Long maxHeap
  Long usedNonHeap
  Long maxNonHeap
  Long usedMemory
  Long maxMemory
  Long usedSwap
  Long maxSwap

  // system parameters
  String name
  String architecture
  String osVersion
  Integer processors

  // load parameters
  Double loadAverage
  Long committedVirtualMemory
  Long processCpuTime


  static belongsTo = [SampleTime, Broker]
  static constraints = {
    committedVirtualMemory(nullable: true)
    maxMemory(nullable: true)
    maxSwap(nullable: true)
    processCpuTime(nullable: true)
    usedMemory(nullable: true)
    usedSwap(nullable: true)
  }
}
