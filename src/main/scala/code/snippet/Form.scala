package code.snippet

import _root_.scala.xml._
import _root_.net.liftweb._
import actor._
import http._
import S._
import SHtml._
import common._
import util._
import Helpers._
import js._
import JsCmds._
import js.jquery._
import JE._

import reactive._
import web._

class Form extends Observing{

  val teachers = List(new Teacher("Ann",1),new Teacher("Debby",2),new Teacher("Paul",3),new Teacher("Dell",4),new Teacher("Mark",5),new Teacher("Mike",6))

  val teacher = Var(teachers.head)

  val teacherHtml = (ns: NodeSeq) => {
    def onChange(s:Int):JsCmd = {
      teacher() = teachers.find(_.id==s).get
      Noop
    }
    disabled_?(ajaxSelectObj[Int](teachers.map(x => (x.id,x.name)),Full(teacher.now.id),onChange _))
  }

  val student = Var(new Student("",0))

  val students = teacher map {x => 
    student() = x.students.head
    x.students
  }

  val studentHtml = {
    def onChange(s:Int):JsCmd = {
      student() = students.now.find(_.id==s).get
      Noop
    }
    web.Cell {
      students map (ss => "* *" #> disabled_?(ajaxSelectObj[Int](ss.map(x => (x.id,x.name)),Full(student.now.id),onChange _)))
    }
  }

  val submitHtml = (ns: NodeSeq) => {
    def onChange: JsCmd = {
      GoTakeANap ! Nap(this, Page.currentPage)
    }
    disabled_?(ajaxButton("Sleep a little",onChange _))
  }

  val in = Var(false)
  def disabled_? = DOMProperty("disabled")(in)

  def render =
    "#teacher" #> teacherHtml &
    "#student" #> studentHtml &
    "#submit" #> submitHtml
}

case class Nap(val snippetInstance:Form,val page:Page)

object GoTakeANap extends LiftActor with ListenerManager with Loggable{
  def createUpdate{}

  override def lowPriority = {
    case Nap(snippetInstance,page) =>
      Reactions.inServerScope(page)(snippetInstance.disabled_?.value() = true)
      Thread.sleep(10000)
      Reactions.inServerScope(page)(snippetInstance.disabled_?.value() = false)

  }
}

/*------------------------------------------------------------------------------------------------*/
class Teacher(val name:String,val id:Int){
  def students = name match {
    case "Ann" => List(new Student("Abby",1),new Student("Addison",2),new Student("Buddy",3),new Student("Dianna",4),new Student("Grayson",5),new Student("Lucy",6))
    case "Debby" => List(new Student("Esther",7),new Student("Kate",8),new Student("Madison",9))
    case "Paul" => List(new Student("Nikki",10),new Student("Jane",11),new Student("Page",12),new Student("Buffy",13))
    case "Dell" => List(new Student("Callie",14),new Student("Quinn",15),new Student("Paul",16),new Student("Dell",17),new Student("Adolph",18))
    case "Mark" => List(new Student("Robbie",19),new Student("Dolly",20))
    case "Mike" => List(new Student("Adela",21),new Student("Sissy",22),new Student("Tracy",23),new Student("Tricia",24),new Student("Zach",25),new Student("Adelaide",26))
  }
}

class Student(val name:String,val id:Int)

