package impressionbit.peoplebook.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Source {

    @JsonProperty("results")
    private List<Results> results = new ArrayList<Results>();

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }
}
