package org.myftp.gattserver.csi.relations

import _root_.org.myftp.gattserver.csi.world.{ RelationType, Person, World }
import org.myftp.gattserver.csi.world.ApplicableRelationType

abstract class AbstractRelationType extends ApplicableRelationType {

  def apply(holdingPerson: Person, targetPerson: Person, world: World): Boolean = {

    // test existence daného vztahu
    if (world.knowledge.relationsByRelation.contains(this)) {
      val relations = world.knowledge.relationsByRelation(this);
      if (relations.contains(targetPerson)
        && relations(targetPerson).equals(holdingPerson))
        return false;
    }

    return applyRelation(holdingPerson, targetPerson, world);
  }

  def applyRelation(holdingPerson: Person, targetPerson: Person, world: World): Boolean;

  override def hashCode(): Int = {
    return name.hashCode();
  }

  override def equals(obj: Any): Boolean = {
    if (obj.isInstanceOf[RelationType]) {
      return obj.asInstanceOf[RelationType].name == name;
    }
    return false;
  }

}