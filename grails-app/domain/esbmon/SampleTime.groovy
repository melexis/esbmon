package esbmon

import org.joda.time.DateTime

class SampleTime {

  DateTime sampleTime

  boolean operatingComplete = false;

  boolean networkComplete = false;

  static constraints = {
  }

  String toString() { sampleTime.toString() }
}
