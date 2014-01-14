package org.myftp.gattserver.csi.world.factories

import org.slf4j.LoggerFactory
import org.myftp.gattserver.csi.world.Person
import org.myftp.gattserver.csi.world.World
import org.myftp.gattserver.csi.world.RelationType
import org.myftp.gattserver.csi.palette.RelationTypesRegister
import scala.util.Random
import org.myftp.gattserver.csi.world.Person
import org.myftp.gattserver.csi.world.ApplicableRelationType

object RelationFactory {

  val logger = LoggerFactory.getLogger(RelationFactory.getClass());

  /**
   * Generuje vztahy iniciovan� vybranou osobou. Proch�z� postupn� v�echny
   * typy vztah� a n�hodn� je realizuje na vybran� osob� a n�hodn� vybran�
   * c�lov� osob� (p��jemce vztahu)
   *
   * @param holdingPerson
   * @param persons
   * @param allowedRelations
   */
  def generateRelations(holdingPerson: Person, world: World, allowedRelations: scala.collection.mutable.Set[ApplicableRelationType] = RelationTypesRegister.relationTypes) {

    val persons = world.persons.toSeq;

    for (relationType <- allowedRelations) {

      // n�hodn� vyber c�lovou osobu
      var targetPerson: Person = null;
      while (targetPerson == null || holdingPerson.equals(targetPerson) == true) {
        targetPerson = persons(Random.nextInt(persons.size));
      }

      if (relationType(holdingPerson, targetPerson, world)) {
        logger.info(holdingPerson.firstName + " " + holdingPerson.sureName + " --> "
          + relationType.name + " --> " + targetPerson.firstName + " "
          + targetPerson.sureName);
        world.registerRelation(relationType, holdingPerson, targetPerson);
      }
    }

  }

}