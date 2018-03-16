package common.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by dliu15 on 3/11/18.
 */
public class Seat implements Serializable {

        private int rowNum;
        private int colNum;

        public int getRowNum() {
            return rowNum;
        }

        public void setRowNum(int rowNum) {
            this.rowNum = rowNum;
        }

        public int getColNum() {
            return colNum;
        }

        public void setColNum(int colNum) {
            this.colNum = colNum;
        }

        public Seat(int rowNum, int colNum) {
            this.rowNum=rowNum;
            this.colNum=colNum;
        }

        @Override
        public String toString() {
            return "Seat{" +
                    "rowNum=" + rowNum +
                    ", colNum=" + colNum +
                    "}";
        }


}
