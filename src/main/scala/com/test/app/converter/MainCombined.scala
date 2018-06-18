package com.test.app.converter

object MainCombined {
  package example

  import java.util

  object Example extends App {

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
      files.add(status)

      map.put("files", files)
    }

    //  example1()
    example2()

    println(s"Original: $map")

    def update(m:util.Map[String, Object]): util.Map[String, Object] = {
      var result: util.Map[String, Object] = new util.HashMap[String, Object]()

      m.entrySet().forEach(x => {
        if (x.getValue.isInstanceOf[String]) {
          result.put(x.getKey, x.getValue)
        } else if (x.getValue.isInstanceOf[util.List[Object]]){
          //transform inner list
          result.put(x.getKey, transformInnerList(x.getValue.asInstanceOf[util.List[Object]]))
        } else if (x.getValue.isInstanceOf[util.Map[Object, Object]]){
          //transform inner map
          result.putAll(transformInnerMap(x.getKey, x.getValue.asInstanceOf[util.Map[String, Object]]))
        }
      })

      result
    }

    private def transformInnerMap(rootKey:String, map:util.Map[String, Object]): util.Map[String, Object] = {
      var result: util.Map[String, Object] = new util.HashMap[String, Object]()

      if (rootKey.charAt(0).isUpper) {
        result.put("type", rootKey)
        result.putAll(update(map))
      } else {
        result.put(rootKey, update(map))
      }

      result
    }

    private def transformInnerList(list: util.List[Object]): util.List[Object] = {
      var result: util.List[Object] = new util.ArrayList[Object]()

      list.forEach(l => {
        if (l.isInstanceOf[String]) {
          result.add(l.asInstanceOf[String])
        } else if (l.isInstanceOf[util.Map[Object, Object]]){
          result.add(update(l.asInstanceOf[util.Map[String, Object]]))
        }
      })

      result
    }

    println(s"Updated: ${update(map)}")

  }

}
