package org.myftp.gattserver.csi.relations

import org.myftp.gattserver.csi.world.Knowledge
import org.myftp.gattserver.csi.world.Person
import org.myftp.gattserver.csi.relations.types.tagging._
import org.myftp.gattserver.csi.relations.types._
import org.myftp.gattserver.csi.world.World
import scala.collection.mutable.HashSet
import org.myftp.gattserver.csi.world.Knowledge

object ChildRelationType {
  val MIN_AGE_DIFF_RANGE = 18;
}

abstract class ChildRelationType extends AbstractRelationType {

  // vytvoøené vztahy se doèasnì uloží sem - pak se pøesypou do knowledge
  // všech zúèastnìných
  val awareKnowledge = new Knowledge();
  // zúèastnìní lidé, kterým je potøeba dát vìdìt o novì vzniklých vztazích
  val awarePersons = new HashSet[Person];

  def countPropability(holdingPerson: Person, targetPerson: Person, world: World): Double = 0.5

  def relationDepthLimitationCheck(holdingPerson: Person, worldKnowledge: Knowledge): Boolean = {
    // OMEZENÍ HLOUBKY VZTAHÙ (jinak by mi z toho asi jeblo)
    // pokud osoba který má být synem/dcerou už má vlastní rodinu,
    // tedy má nìjakého syna nebo dceru, nepropojuj jí dál nahoru
    // výsledkem by bylo totiž procházení dìdù, vnuèek apod., což
    // vede prakticky ke "vztahové explozi", která sice je
    // implementovatelná, ale aktuálnì by se na tom jen zabíjel èas,
    // což v rámci psaní prototypu není žádoucí
    // TODO odstranit a rozšíøit o další aware vztahy a lidi, až
    // tento program nebude prototyp ...
    var myOwnChildren: HashSet[Person] = null
    if (holdingPerson.male) {
      myOwnChildren = worldKnowledge.related(holdingPerson, Father);
    } else {
      myOwnChildren = worldKnowledge.related(holdingPerson, Mother);
    }
    if (myOwnChildren != null && myOwnChildren.empty == false)
      return false;

    return true;
  }

  def applyRelation(holdingPerson: Person, targetPerson: Person, world: World): Boolean = {

    val worldKnowledge = world.knowledge;

    if (holdingPerson.male) {
      if (this == Daughter)
        return false;
    } else {
      if (this == Son)
        return false;
    }

    if (relationDepthLimitationCheck(holdingPerson, worldKnowledge) == false)
      return false;

    val hPAge = holdingPerson.age;
    val tPAge = targetPerson.age;

    // vìkové rozdíly
    if (tPAge > hPAge + ChildRelationType.MIN_AGE_DIFF_RANGE)
      return false;

    if (worldKnowledge.checkBannedRelations(holdingPerson, targetPerson, Sister,
      Brother, Cousin, Aunt, Uncle) == false)
      return false;

    // ok, kontroly prošly - pøidej první,
    // kteøí budou obeznámeni s novými vztahy
    awarePersons.add(holdingPerson);
    awarePersons.add(targetPerson);

    // sourozenci
    val siblings = new HashSet[Person];
    var sons = worldKnowledge.related(targetPerson, Son);
    var daughters = worldKnowledge.related(targetPerson, Daughter);
    if (sons != null)
      siblings.++=(sons);
    if (daughters != null)
      siblings.++=(daughters);

    // pøidej se jako potomek
    awareKnowledge.registerRelation(this, holdingPerson, targetPerson);

    // matka/otec
    awareKnowledge.registerRelation(if (targetPerson.male) Father else Mother,
      targetPerson, holdingPerson);
    val related = worldKnowledge.related(targetPerson, if (targetPerson.male) Wife
    else Husband);
    if (related != null) {
      for (partner <- related) {

        // ani partner nesmí porušit limit hloubky rodinných vztahù :P
        if (relationDepthLimitationCheck(partner, worldKnowledge) == false)
          return false;

        // najdi sourozence získané od partnera mého biologického rodièe
        sons = worldKnowledge.related(partner, Son);
        daughters = worldKnowledge.related(partner, Daughter);
        if (sons != null)
          siblings.++=(sons);
        if (daughters != null)
          siblings.++=(daughters);

        // pøidej se jako potomek partnera mého biologického rodièe
        awareKnowledge.registerRelation(this, holdingPerson, partner);
        awareKnowledge.registerRelation(if (targetPerson.male) Mother else Father,
          partner, holdingPerson);
      }
      awarePersons.++=(related);
    }

    // sourozenec
    awarePersons.++=(siblings);
    for (sibling <- siblings) {
      // já jemu
      awareKnowledge.registerRelation(if (holdingPerson.male) Brother else Sister,
        holdingPerson, sibling);
      // on/ona mì
      awareKnowledge.registerRelation(if (sibling.male) Brother else Sister, sibling,
        holdingPerson);
    }

    writeKnowledgeToInvolved(worldKnowledge);

    return true;
  }

  def writeKnowledgeToInvolved(worldKnowledge: Knowledge) {
    val relations = awareKnowledge.relationsByRelation;
    for (relationType <- relations.keySet) {
      val psp = relations(relationType);
      for (holdingPerson <- psp.keySet) {
        val targetPersons = psp(holdingPerson);
        for (targetPerson <- targetPersons) {
          for (awarePerson <- awarePersons) {
            awarePerson.knowledge.registerRelation(relationType, holdingPerson, targetPerson);
          }
          worldKnowledge.registerRelation(relationType, holdingPerson, targetPerson);
        }
      }
    }
  }
}