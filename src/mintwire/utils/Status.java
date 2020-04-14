/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintwire.utils;
/**
 *
 * @author Lavinia
 */
public enum Status {
  available{
        public String toString() {
          return "available";
      }
  },
  away{
        public String toString() {
          return "away";
      }
  },
  donotdisturb{
        public String toString() {
          return "donotdisturb";
      }
  },
  invisible{
        public String toString() {
          return "invisible";
      }
  }
}
