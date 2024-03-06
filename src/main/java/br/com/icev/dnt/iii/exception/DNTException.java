package br.com.icev.dnt.iii.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author jonny
 */
public class DNTException extends Exception {

    private final List<String> exceptions = new ArrayList<>();

    public DNTException() {
    }

    public DNTException(String message) {
        super(message);
    }

    public void add(String exception) {
        exceptions.add(exception);
    }

    public void check() throws DNTException {
        if (!CollectionUtils.isEmpty(exceptions)) {
            throw new DNTException(exceptions.toString().replace("[", "").replace("]", ""));
        }
    }
}
