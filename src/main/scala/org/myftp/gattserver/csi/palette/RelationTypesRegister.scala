package org.myftp.gattserver.csi.palette

import org.myftp.gattserver.csi.world.ApplicableRelationType
import scala.collection.mutable.HashSet
import org.myftp.gattserver.csi.relations.types._
import org.slf4j.LoggerFactory

object RelationTypesRegister {

  val logger = LoggerFactory.getLogger(RelationTypesRegister.getClass());

  val relationTypes = new HashSet[ApplicableRelationType];

  def registerType(relationTypes: ApplicableRelationType*) {
    //relationTypes.foreach(this.relationTypes += _) 
    for (relationType <- relationTypes) {
      this.relationTypes += relationType;
      logger.info("RelationType '" + relationType + "' registred")
    }
  }

  // default konstrukt dle balíku vztahù
  registerType(Daughter, Husband, Son, Wife)

  //  val RELATIONS_PACKAGE = "org.myftp.gattserver.csi.relations.types"
  //  Package.getPackage(RELATIONS_PACKAGE).

}