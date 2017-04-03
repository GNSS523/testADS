package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;

public class AirConditionerConstant {

  /*
   *
   TYPE ST_IndoorCondition :
   STRUCT
wOperationCommand:WORD;//=1,write setting value
wOperationMode:WORD;//bit0-sleep;1-swing on;2-indoor mode locked;bit8-off;bit9-cool;bit10-dry;bit11-ventilation;bit12-heat
wFanOperationMode:WORD;//indoor fan:8-off;9-low speed;10-mid speed;11-high speed;12-auto
wSetTemperature:WORD;//

wActualMode:WORD;//bit0-off;bit1-cool;bit2-dry;bit3-ventilation;bit4-heat
wActualSetTemperature:WORD;
wActualTemperature:WORD;
wActualWorkStatus:WORD;//bit0-sleep;1-swing on;2-indoor mode locked;3-normol;4-test;5-reset;6-defrosting;7-antifreez
wActualFaulty:WORD;//bit0-communication error between indoor unit and controller;1-entering coil temperature sensor error;2-leaving sensor error;3-ambient sensor eoor;4-water level switch warning;5-mid sensor error;6-sensor misconection or motor error

END_STRUCT
END_TYPE
   *
   */

  public enum Command implements ICommand{

    ON((byte)1),
    OFF((byte)2),
    COLD((byte)4),
    HOT((byte)8);

    private final byte value;
    Command(byte value){
      this.value = value;
    }

    public byte getValue(){
      return this.value;
    }

    private static final Map<String, Command> lookup = new HashMap<String, Command>();

    static {
      for(Command command : Command.values()){
        lookup.put(command.name(),command );
      }
    }

    public static Command get(String name){
      return lookup.get(name);
    }

    public static byte getValue(String name){
      return lookup.get(name).getValue();
    }

  }


  public enum State {

    OFF((byte)0),
    ON((byte)1),
    COLD((byte)4),
    HOT((byte)8);

    private final byte value;
    State(byte value){
      this.value = value;
    }

    public byte getValue(){
      return this.value;
    }

  }

  /*
     TYPE ST_AirCondition :
     STRUCT
iSetTemper:INT;//HMI设置温度
iCommand:INT;//HMI命令:启动=1;停止=2;制冷=4;制热=8;
iActualTemper:INT;//实际温度
iStatus:INT;//实际状态:停止=0;运行=1;制冷=4;制热=8;

END_STRUCT
END_TYPE
*/

  public enum Table {

    CONFIG_MODE (            (byte)0, (byte)4,  "config.mode"   ,   "CONFIG_MODE",        (byte)-1),
    CONFIG_MODE_STATUS(      (byte)4, (byte)4,  "config.mode"  ,    "CONFIG_MODE_STATUS", (byte)-1),
    CONFIG_TEMPERATURE(      (byte)8, (byte)4,  "command.set.temperature" ,     "CONFIG_TEMPERATURE", (byte)-1),
    GET_TEMPERATURE(         (byte)12, (byte)4, "command.get.temperature",      "GET_TEMPERATURE",    (byte)-1);

    private final byte offset;
    private final byte number;
    private final String type;
    private final String descritpion;
    private final byte id;
    Table(byte offset, byte number, String type, String descritpion, byte id){
      this.offset = offset;
      this.number = number;
      this.type = type;
      this.descritpion = descritpion;
      this.id = id;
    }

    public byte getOffset(){
      return this.offset;
    }
    public byte getNumber(){
      return this.number;
    }
    public String getType(){
      return this.type;
    }
    public String getDescritpion(){
      return this.descritpion;
    }
    public byte getId(){
      return this.id;
    }
    private static final Map<String, Table> lookup = new HashMap<String, Table>();

    static {
      for(Table ads : Table.values()){
        if(ads.getId()!=-1)
          lookup.put(ads.getType()+"_"+ads.getId() ,  ads );
        else
          lookup.put(ads.getType(),ads );
      }
    }

    public static Table get(String name, int id){
      return lookup.get(name+"_"+id);
    }

    public static byte getOffset(String name, int id){
      return lookup.get(name+"_"+id).getOffset();
    }

    public static byte getNumber(String name, int id){
      return lookup.get(name+"_"+id).getNumber();
    }

    public static String getType(String name, int id){
      return lookup.get(name+"_"+id).getType();
    }


    public static Table get(String name){
      return lookup.get(name);
    }

    public static byte getOffset(String name){
      return lookup.get(name).getOffset();
    }

    public static byte getNumber(String name){
      return lookup.get(name).getNumber();
    }

    public static String getType(String name){
      return lookup.get(name).getType();
    }

    public static int getTotalLength(){
      int total = 0;
      for(Table ads : Table.values()){
        total += ads.getNumber();
      }
      return total;
    }
  }
}
