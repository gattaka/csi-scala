package org.myftp.gattserver.csi.world

trait ApplicableRelationType extends RelationType {

  def apply(holdingPerson: Person, targetPerson: Person, world: World): Boolean

  def countPropability(holdingPerson: Person, targetPerson: Person, world: World) : Double
  
}