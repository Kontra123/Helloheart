package assignment.home.helloheart.s3.helloheart.data;

/**
 * Created by AmirG on 5/28/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BloodTestConfigData {

    @SerializedName("bloodTestConfig")
    @Expose
    private List<Details> details = null;

    public List<Details> getBloodTestConfig() {
        return details;
    }

    public void setBloodTestConfig(List<Details> details) {
        this.details = details;
    }

    public class Details {

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("threshold")
        @Expose
        private Integer threshold;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getThreshold() {
            return threshold;
        }

        public void setThreshold(Integer threshold) {
            this.threshold = threshold;
        }

    }

}


