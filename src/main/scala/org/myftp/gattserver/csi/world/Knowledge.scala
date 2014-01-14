package org.myftp.gattserver.csi.world

import scala.collection.mutable.HashSet
import scala.collection.mutable.HashMap

class Knowledge {

  private type PPrelation = HashMap[Person, HashSet[Person]]
  private type RPrelation = HashMap[RelationType, HashSet[Person]]
  private type Persons = HashSet[Person]

  val persons = new Persons;
  val relationsByRelation = new HashMap[RelationType, PPrelation];
  val relationsByPerson = new HashMap[Person, RPrelation];

  def related(holdingPerson: Person, relationType: RelationType): Persons = {
    if (relationsByPerson.contains(holdingPerson) == false) return null;
    val relations = relationsByPerson(holdingPerson);
    return if (relations.contains(relationType)) relations(relationType) else null;
  }

  def isInRelation(holdingPerson: Person, relationType: RelationType, targetPerson: Person = null): Boolean = {
    if (relationsByPerson.contains(holdingPerson) == false) return false;
    val relations = relationsByPerson(holdingPerson);
    if (relations.contains(relationType) == false) return false;
    val persons = relations(relationType);
    if (targetPerson == null)
      return persons.isEmpty == false;
    return persons.contains(targetPerson);
  }

  def checkBannedRelations(holdingPerson: Person, targetPerson: Person, relationTypes: RelationType*): Boolean = {
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
    var relations: PPrelation = if (relationsByRelation.contains(relationType)) relationsByRelation(relationType) else null;
    if (relations == null) {
      relations = new PPrelation;
      relationsByRelation.put(relationType, relations);
    }
    var targetPersons: Persons = if (relations.contains(holdingPerson)) relations(holdingPerson) else null;
    if (targetPersons == null) {
      targetPersons = new Persons;
      relations.put(holdingPerson, targetPersons);
    }
    targetPersons.add(targetPerson);
  }

  private def registerRelationByHoldingPerson(relationType: RelationType, holdingPerson: Person, targetPerson: Person) {
    var relations: RPrelation = if (relationsByPerson.contains(holdingPerson)) relationsByPerson(holdingPerson) else null;
    if (relations == null) {
      relations = new RPrelation;
      relationsByPerson.put(holdingPerson, relations);
    }
    var targetPersons: Persons = if (relations.contains(relationType)) relations(relationType) else null;
    if (targetPersons == null) {
      targetPersons = new Persons;
      relations.put(relationType, targetPersons);
    }
    targetPersons.add(targetPerson);
  }

}