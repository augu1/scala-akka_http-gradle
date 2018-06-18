package com.test.app.converter

import java.util

object Main extends App {

  println("Hello World")

  var map: util.Map[String, Object] = new util.HashMap[String, Object]()
  var success: util.Map[String, Object] = new util.HashMap[String, Object]()
  var status: util.Map[String, Object] = new util.HashMap[String, Object]()

  status.put("Success", success)
  success.put("done", "doneValue")
  success.put("done2", "doneValue2")

  def example1(): Unit = {
    map.put("halo", "haloValue")
    map.put("status", status)
  }

  def example2(): Unit = {
    example1()
    var files: util.List[Object] = new util.ArrayList[Object]()

    files.add(status)

    map.put("files", files)
  }

  example1()
  //  example2()

  println(s"Original: $map")

  def update(m:util.Map[String, Object]): util.Map[String, Object] = {
    var result: util.Map[String, Object] = new util.HashMap[String, Object]()

    m.entrySet().forEach(x => {
      if (x.getValue.isInstanceOf[String]) {
        result.put(x.getKey, x.getValue)
      } else if (x.getValue.isInstanceOf[util.List[Object]]){
        //        println("list")
        //transform inner list
        var updatedList: util.List[Object] = new util.ArrayList[Object]()
        x.getValue.asInstanceOf[util.List[Object]].forEach(l => {
          if (l.isInstanceOf[String]) {
            updatedList.add(l.asInstanceOf[String])
          } else if (l.isInstanceOf[util.Map[Object, Object]]){
            updatedList.add(update(l.asInstanceOf[util.Map[String, Object]]))
          }
        })
        result.put(x.getKey, updatedList)
      } else if (x.getValue.isInstanceOf[util.Map[Object, Object]]){
        //transform inner map
        var updatedMap: util.Map[String, Object] = x.getValue.asInstanceOf[util.Map[String, Object]]
        //        println(s"mapa ${x.getValue}")
        if (x.getKey.charAt(0).isUpper) {
          result.put("type", x.getKey)
          result.putAll(update(updatedMap))
        } else {
          result.put(x.getKey, update(updatedMap))
        }
      }
    })

    result
  }

  println(s"Updated: ${update(map)}")


}
