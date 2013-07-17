/*
   Copyright 2013 Technical University of Denmark, DTU Compute. 
   All rights reserved.
   
   This file is part of the time-predictable VLIW processor Patmos.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are met:

      1. Redistributions of source code must retain the above copyright notice,
         this list of conditions and the following disclaimer.

      2. Redistributions in binary form must reproduce the above copyright
         notice, this list of conditions and the following disclaimer in the
         documentation and/or other materials provided with the distribution.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ``AS IS'' AND ANY EXPRESS
   OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
   OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
   NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
   THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

   The views and conclusions contained in the software and documentation are
   those of the authors and should not be interpreted as representing official
   policies, either expressed or implied, of the copyright holder.
 */

/*
 * Definitions for OCP as used in Patmos
 * 
 * Authors: Wolfgang Puffitsch (wpuffitsch@gmail.com)
 * 
 */

package ocp

import Chisel._
import Node._

// Constants for MCmd
object OcpCmd {
  val IDLE = Bits("b000")
  val WR   = Bits("b001")
  val RD   = Bits("b010")
  val RDEX = Bits("b011")
  val RDL  = Bits("b100")
  val WRNP = Bits("b101")
  val WRC  = Bits("b110")
  val BCST = Bits("b111")
}

// Constants for SResp
object OcpResp {
  val NULL = Bits("b00")
  val DVA  = Bits("b01")
  val FAIL = Bits("b10")
  val ERR  = Bits("b11")
}

// Signals generated by master
class OcpMasterSignals(addrWidth : Int, dataWidth : Int) extends Bundle() {
  val Cmd = Bits(width = 3)
  val Addr = Bits(width = addrWidth)
  val Data = Bits(width = dataWidth)
  val ByteEn = Bits(width = dataWidth/8)

  // This does not really clone, but Data.clone doesn't either
  override def clone() = {
    val res = new OcpMasterSignals(addrWidth, dataWidth)
  	res.asInstanceOf[this.type]
  }
}

// Reset values for master signals
object OcpMasterSignals {
  def resetVal(sig : OcpMasterSignals) : OcpMasterSignals = {
	val res = sig.clone
	res.Cmd := OcpCmd.IDLE
	res.Addr := Bits(0)
	res.Data := Bits(0)
	res.ByteEn := Bits(0)
	res
  }
  def resetVal(addrWidth : Int, dataWidth : Int) : OcpMasterSignals = {
	resetVal(new OcpMasterSignals(addrWidth, dataWidth))
  }
}

// Signals generated by slave
class OcpSlaveSignals(dataWidth : Int) extends Bundle() {
  val Resp = Bits(width = 2)
  val Data = Bits(width = dataWidth)

  // This does not really clone, but Data.clone doesn't either
  override def clone() = {
    val res = new OcpSlaveSignals(dataWidth)
  	res.asInstanceOf[this.type]
  }
}

// Reset values for slave signals
object OcpSlaveSignals {
  def resetVal(sig : OcpSlaveSignals) : OcpSlaveSignals = {
	val res = sig.clone
	res.Resp := OcpResp.NULL
	res.Data := Bits(0)
	res
  }
  def resetVal(addrWidth : Int, dataWidth : Int) : OcpSlaveSignals = {
	resetVal(new OcpSlaveSignals(dataWidth))
  }
}

// Master port
class OcpMasterPort(addrWidth : Int, dataWidth : Int) extends Bundle() {
  // Clk is implicit in Chisel
  val M = new OcpMasterSignals(addrWidth, dataWidth).asOutput
  val S = new OcpSlaveSignals(dataWidth).asInput
}

// Slave port is reverse of master port
class OcpSlavePort(addrWidth : Int, dataWidth : Int) extends Bundle() {
  // Clk is implicit in Chisel
  val M = new OcpMasterSignals(addrWidth, dataWidth).asInput
  val S = new OcpSlaveSignals(dataWidth).asOutput
}
