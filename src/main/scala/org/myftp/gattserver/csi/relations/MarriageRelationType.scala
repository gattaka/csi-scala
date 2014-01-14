package org.myftp.gattserver.csi.relations

import org.myftp.gattserver.csi.world.Knowledge
import org.myftp.gattserver.csi.world.Person
import org.myftp.gattserver.csi.relations.types.tagging._
import org.myftp.gattserver.csi.relations.types._
import org.myftp.gattserver.csi.world.World
import scala.collection.mutable.HashSet
import org.myftp.gattserver.csi.world.Knowledge
import org.myftp.gattserver.csi.world.RelationType

object MarriageRelationType {
  val MAX_AGE_DIFF_RANGE = 20;
  val MIN_AGE = 18;
}

abstract class MarriageRelationType extends AbstractRelationType {

  def countPropability(holdingPerson: Person, targetPerson: Person, world: World): Double = 0.1

  def applyRelation(holdingPerson: Person, targetPerson: Person, world: World): Boolean = {

    val worldKnowledge = world.knowledge;

    if (holdingPerson.male) {
      if (this == Wife)
        return false;
    } else {
      if (this == Husband)
        return false;
    }

    val hPAge = holdingPerson.age;
    val tPAge = targetPerson.age;

    // vìkové rozdíly
    if (Math.abs(tPAge - hPAge) > MarriageRelationType.MAX_AGE_DIFF_RANGE)
      return false;

    if (hPAge < MarriageRelationType.MIN_AGE || tPAge < MarriageRelationType.MIN_AGE)
      return false;

    if (worldKnowledge.checkBannedRelations(holdingPerson, targetPerson, Sister,
      Brother, Cousin, Aunt, Uncle, Father, Mother) == false)
      return false;

    // pøidej se
    world.registerPublicRelation(this, holdingPerson, targetPerson);

    // synové a dcery mého partnera jsou nyní i moji synové
    for (partnerChildRelationType <- List(Son, Daughter)) {
      for (partnerChild <- worldKnowledge.related(targetPerson, partnerChildRelationType)) {
        world.registerPublicRelation(if (partnerChild.male) Son else Daughter, partnerChild, holdingPerson)
        world.registerPublicRelation(if (holdingPerson.male) Father else Mother, holdingPerson, partnerChild)
        // moje dìti jsou teï jejich sestry a bratøi
        for (myChildrenRelationType <- List(Son, Daughter)) {
          for (myChild <- worldKnowledge.related(holdingPerson, myChildrenRelationType)) {
            world.registerPublicRelation(if (partnerChild.male) Brother else Sister, partnerChild, myChild)
            world.registerPublicRelation(if (myChild.male) Brother else Sister, myChild, partnerChild)
          }
        }
        // moje sestry a bratøi jsou teï jejich tety a strýcové 
        for (mySiblingRelationType <- List(Sister, Brother)) {
          for (mySibling <- worldKnowledge.related(holdingPerson, mySiblingRelationType)) {
            world.registerPublicRelation(if (mySibling.male) Uncle else Aunt, mySibling, partnerChild)
          }
        }
      }
    }

    return true;
  }

}