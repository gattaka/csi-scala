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

  // vytvo�en� vztahy se do�asn� ulo�� sem - pak se p�esypou do knowledge
  // v�ech z��astn�n�ch
  val awareKnowledge = new Knowledge();
  // z��astn�n� lid�, kter�m je pot�eba d�t v�d�t o nov� vznikl�ch vztaz�ch
  val awarePersons = new HashSet[Person];

  def countPropability(holdingPerson: Person, targetPerson: Person, world: World): Double = 0.5

  def relationDepthLimitationCheck(holdingPerson: Person, worldKnowledge: Knowledge): Boolean = {
    // OMEZEN� HLOUBKY VZTAH� (jinak by mi z toho asi jeblo)
    // pokud osoba kter� m� b�t synem/dcerou u� m� vlastn� rodinu,
    // tedy m� n�jak�ho syna nebo dceru, nepropojuj j� d�l nahoru
    // v�sledkem by bylo toti� proch�zen� d�d�, vnu�ek apod., co�
    // vede prakticky ke "vztahov� explozi", kter� sice je
    // implementovateln�, ale aktu�ln� by se na tom jen zab�jel �as,
    // co� v r�mci psan� prototypu nen� ��douc�
    // TODO odstranit a roz���it o dal�� aware vztahy a lidi, a�
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

    // v�kov� rozd�ly
    if (tPAge > hPAge + ChildRelationType.MIN_AGE_DIFF_RANGE)
      return false;

    if (worldKnowledge.checkBannedRelations(holdingPerson, targetPerson, Sister,
      Brother, Cousin, Aunt, Uncle) == false)
      return false;

    // ok, kontroly pro�ly - p�idej prvn�,
    // kte�� budou obezn�meni s nov�mi vztahy
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

    // p�idej se jako potomek
    awareKnowledge.registerRelation(this, holdingPerson, targetPerson);

    // matka/otec
    awareKnowledge.registerRelation(if (targetPerson.male) Father else Mother,
      targetPerson, holdingPerson);
    val related = worldKnowledge.related(targetPerson, if (targetPerson.male) Wife
    else Husband);
    if (related != null) {
      for (partner <- related) {

        // ani partner nesm� poru�it limit hloubky rodinn�ch vztah� :P
        if (relationDepthLimitationCheck(partner, worldKnowledge) == false)
          return false;

        // najdi sourozence z�skan� od partnera m�ho biologick�ho rodi�e
        sons = worldKnowledge.related(partner, Son);
        daughters = worldKnowledge.related(partner, Daughter);
        if (sons != null)
          siblings.++=(sons);
        if (daughters != null)
          siblings.++=(daughters);

        // p�idej se jako potomek partnera m�ho biologick�ho rodi�e
        awareKnowledge.registerRelation(this, holdingPerson, partner);
        awareKnowledge.registerRelation(if (targetPerson.male) Mother else Father,
          partner, holdingPerson);
      }
      awarePersons.++=(related);
    }

    // sourozenec
    awarePersons.++=(siblings);
    for (sibling <- siblings) {
      // j� jemu
      awareKnowledge.registerRelation(if (holdingPerson.male) Brother else Sister,
        holdingPerson, sibling);
      // on/ona m�
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