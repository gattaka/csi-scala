package org.myftp.gattserver.csi.world

trait RelationType {

  var name : String;
  
  def applyRelation(holdingPerson:Person, targetPerson: Person ) : Boolean;

}