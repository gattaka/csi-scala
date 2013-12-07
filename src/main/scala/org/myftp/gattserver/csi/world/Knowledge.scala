package org.myftp.gattserver.csi.world

import scala.collection.mutable.HashSet
import scala.collection.mutable.HashMap

class Knowledge {

  val persons = new HashSet[Person];
  val relationsByRelation = new HashMap[RelationType, HashMap[Person, HashSet[Person]]];
  val relationsByPerson = new HashMap[Person, HashMap[RelationType, HashSet[Person]]];

  def related(holdingPerson: Person, relationType: RelationType): HashSet[Person] = {
    val relations = relationsByPerson(holdingPerson);
    if (relations == null) return null;
    return relations(relationType);
  }

  def isInRelation(holdingPerson: Person, relationType: RelationType, targetPerson: Person = null): Boolean = {
    val relations = relationsByPerson(holdingPerson);
    if (relations == null) return false;
    val persons = relations(relationType);
    if (persons == null) return false;
    if (targetPerson == null)
      return persons.isEmpty == false;
    return persons.contains(targetPerson);
  }

  def checkBannedRelations(holdingPerson: Person, targetPerson: Person, relationTypes: List[RelationType]): Boolean = {
    for (relationType <- relationTypes) {
      val persons = related(holdingPerson, relationType);
      if (persons != null) {
        if (persons.contains(targetPerson)) return false;
      }
    }
    return true;
  }

  def registerRelation(relationType: RelationType, holdingPerson: Person, targetPerson: Person) {
    registerRelationByRelation(relationType, holdingPerson, targetPerson);
    registerRelationByHoldingPerson(relationType, holdingPerson, targetPerson);
  }

  /*
   * PRIVATE metody
   */

  private def removeRelationError() {
    throw new IllegalStateException("Trying to remove nonexisting relation");
  }

  private def registerRelationByRelation(relationType: RelationType, holdingPerson: Person, targetPerson: Person) {
    var relations = relationsByRelation(relationType);
    if (relations == null) {
      relations = new HashMap[Person, HashSet[Person]];
      relationsByRelation.put(relationType, relations);
    }
    var targetPersons = relations(holdingPerson);
    if (targetPersons == null) {
      targetPersons = new HashSet[Person];
      relations.put(holdingPerson, targetPersons);
    }
    targetPersons.add(targetPerson);
  }

  private def registerRelationByHoldingPerson(relationType: RelationType, holdingPerson: Person, targetPerson: Person) {
    var relations = relationsByPerson(holdingPerson);
    if (relations == null) {
      relations = new HashMap[RelationType, HashSet[Person]];
      relationsByPerson.put(holdingPerson, relations);
    }
    var targetPersons = relations(relationType);
    if (targetPersons == null) {
      targetPersons = new HashSet[Person];
      relations.put(relationType, targetPersons);
    }
    targetPersons.add(targetPerson);
  }

}