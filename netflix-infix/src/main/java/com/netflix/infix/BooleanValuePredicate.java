package com.netflix.infix;

import javax.annotation.Nullable;

import com.google.common.base.Objects;

public class BooleanValuePredicate implements ValuePredicate<Boolean> {

	private Boolean value;
	
	private BooleanValuePredicate(@Nullable Boolean value){
		this.value = value;
	}
	
	@Override
    public boolean apply(@Nullable Boolean input) {
	    return Objects.equal(value, input);
    }

	public Boolean getValue(){
		return value;
	}

	public static final BooleanValuePredicate TRUE = new BooleanValuePredicate(Boolean.TRUE);
	
	public static final BooleanValuePredicate FALSE = new BooleanValuePredicate(Boolean.FALSE);
	
	@Override
    public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("BooleanValuePredicate [value=");
	    builder.append(value);
	    builder.append("]");
	    return builder.toString();
    }

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((value == null) ? 0 : value.hashCode());
	    return result;
    }

	@Override
    public boolean equals(Object obj) {
	    if (this == obj) {
		    return true;
	    }
	    if (obj == null) {
		    return false;
	    }
	    if (getClass() != obj.getClass()) {
		    return false;
	    }
	    BooleanValuePredicate other = (BooleanValuePredicate) obj;
	    if (value == null) {
		    if (other.value != null) {
			    return false;
		    }
	    } else if (!value.equals(other.value)) {
		    return false;
	    }
	    return true;
    }
}
