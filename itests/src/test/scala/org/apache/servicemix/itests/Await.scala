/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.servicemix.itests

/**
 * Convenience trait to wait for a certain condition to occur by retrying it a few times,
 * using an exponential back-off delay scheme
 */
trait Await {

  val INITIAL_DELAY = 125
  val MAXIMUM_DELAY = 8000

  def await[T](condition: => Option[T]) : Option[T] = await(condition, INITIAL_DELAY)

  private[this] def await[T](condition: => Option[T], delay: Long) : Option[T] =
    condition match {
      case result@Some(_) => result
      case None           => if (delay > MAXIMUM_DELAY) None else {
        // let's sleep for a while and give it another go
        Thread.sleep(delay)
        await(condition, delay * 2)
      }
    }

}
