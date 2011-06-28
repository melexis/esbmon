package esbmon

import org.joda.time.DateTime

class SampleTime {

  DateTime sampleTime

  boolean complete = false;

  static constraints = {
  }

  String toString() { sampleTime.toString() }
}
