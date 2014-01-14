package org.myftp.gattserver.csi.world.factories

import scala.util.Random
import org.myftp.gattserver.csi.palette.SureName

object SureNameFactory {

  private val sureNames = List("SMITH", "JONES", "WILLIAMS", "TAYLOR", "BROWN", "DAVIES", "EVANS", "WILSON", "THOMAS", "JOHNSON");

  private val random = new Random();

  def getRandomName(): SureName = {
    val randomIndex = random.nextInt(sureNames.size);
    return SureName(sureNames(randomIndex));
  }

}