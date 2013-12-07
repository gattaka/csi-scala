package org.myftp.gattserver.csi.palette.factories

import scala.util.Random
import org.myftp.gattserver.csi.palette.FirstName

object FirstNameFactory {

  private val maleNames = List("ADAM", "JIM", "JACK", "PETER", "MARK", "PAUL", "DAVID", "JOHN", "WILLIAM", "ROBERT", "RICHARD", "HENRY");
  private val femaleNames = List("ADELE", "ALEX", "ALICE", "REGINA", "MARY", "RONA", "ROSE", "RYANNE", "SAL", "SARA");

  private val random = new Random();

  def getRandomName(male: Boolean): FirstName = {
    val list = if (male) maleNames else femaleNames;
    val randomIndex = random.nextInt(list.size);
    return FirstName(list(randomIndex));
  }

}