package org.myftp.gattserver.csi.world

import scala.collection.mutable.HashSet
import scala.collection.Set

class World {

  var population = 0;
  var numberOfMales = 0;
  var numberOfFemales = 0;
  var averageAge = 0.0;
  var minimumAge = Double.MaxValue;
  var maximumAge = 0.0;
  var sumOfAges = 0.0;

  var yearOffset = 0;

  val knowledge = new Knowledge();

  def relationTypes: Set[RelationType] = {
    return knowledge.relationsByRelation.keySet;
  }

  def registerRelation(relationType: RelationType,
                       holdingPerson: Person, targetPerson: Person) {
    knowledge.registerRelation(relationType, holdingPerson, targetPerson);
  }

  def registerPerson(person: Person) {
    population += 1;
    if (person.male) {
      numberOfMales += 1;
    } else {
      numberOfFemales += 1;
    }
    val age = person.age;
    sumOfAges += age;
    if (age > maximumAge)
      maximumAge = age;
    if (age < minimumAge)
      minimumAge = age;

    averageAge = sumOfAges / population;
    knowledge.persons += person;
  }

  def registerPublicRelation(relationType: RelationType,
                             holdingPerson: Person, targetPerson: Person) {
    knowledge.registerRelation(relationType, holdingPerson, targetPerson);
    for (person <- persons) {
      person.knowledge.registerRelation(relationType, holdingPerson, targetPerson);
    }
  }

  /**
   * Gettery
   */

  def persons = {
    knowledge.persons;
  }

  def personsByRelations(relationType: RelationType) = {
    knowledge.relationsByRelation(relationType);
  }

}