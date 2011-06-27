package esbmon

import org.joda.time.DateTime


class UpdateStatisticsJob {
  def timeout = 60000l // execute job once in 5 seconds

  def execute() {
    SampleTime smplTime = new SampleTime(sampleTime: new DateTime())

  }
}
