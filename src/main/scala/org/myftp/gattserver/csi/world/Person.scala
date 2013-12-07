package org.myftp.gattserver.csi.world

import org.myftp.gattserver.csi.palette.Address
import java.util.Date
import org.myftp.gattserver.csi.palette.FirstName
import org.myftp.gattserver.csi.palette.SureName
import org.joda.time.LocalDate
import org.joda.time.Months
import java.text.SimpleDateFormat
import java.text.DecimalFormat

case class Person(firstName: FirstName, sureName: SureName,
  maidenName: SureName,
  birthDate: Date,
  address: Address,
  male: Boolean) {

  //  val firstName: FirstName;
  //  val sureName: SureName;
  //  val maidenName: SureName;
  //  val birthDate: Date;
  //  val address: Address;
  //  val male: Boolean;

  val knowledge: Knowledge = new Knowledge();
  val fingerprint: Int = hashCode();

  def age: Double = {
    return age(0);
  }

  def age(yearOffset: Integer): Double = {
    val date = LocalDate.now();
    val birthDate = LocalDate.fromDateFields(this.birthDate);
    return Months.monthsBetween(birthDate, date).getMonths() / 12.0 - yearOffset * 12;
  }

  override def equals(obj: Any): Boolean = {
    return obj.isInstanceOf[Person] && (obj.asInstanceOf[Person].fingerprint == fingerprint);
  }

  override def hashCode: Int = {
    return fingerprint;
  }

  override def toString: String = {
    val dateFormat = new SimpleDateFormat("d.M.yyyy");
    val numberFormat = new DecimalFormat("###.##");
    return "I am " + firstName + " " + sureName + " from " + address + " (" +
      (if (male) "male" else "female") + "), born on " + dateFormat.format(birthDate) +
      " (age " + numberFormat.format(age) + ") - my fingerprint is " + fingerprint
  }

}