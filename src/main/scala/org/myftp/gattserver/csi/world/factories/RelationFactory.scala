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
   * Generuje vztahy iniciované vybranou osobou. Prochází postupnì všechny
   * typy vztahù a náhodnì je realizuje na vybrané osobì a náhodnì vybrané
   * cílové osobì (pøíjemce vztahu)
   *
   * @param holdingPerson
   * @param persons
   * @param allowedRelations
   */
  def generateRelations(holdingPerson: Person, world: World, allowedRelations: scala.collection.mutable.Set[ApplicableRelationType] = RelationTypesRegister.relationTypes) {

    val persons = world.persons.toSeq;

    for (relationType <- allowedRelations) {

      // náhodnì vyber cílovou osobu
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