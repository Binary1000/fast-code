package ${packageRoot}.enums;

/**
 * @author Binary on 2020/6/3
 */
public enum ${EnumClassName} {

#foreach(${enum} in ${enums})
    ${enum.name}(#if($valueType=="String")"${enum.value}"#else${enum.value}#end)#if($velocityHasNext),#else;#end

#end

    private ${valueType} value;

    ${EnumClassName}(${valueType} value) {
        this.value = value;
    }

    public ${valueType} value() {
        return value;
    }

    public static ${EnumClassName} of(${valueType} value) {
        ${EnumClassName}[] values = values();

        for (${EnumClassName} ${enumClassName} : values) {
            if (${enumClassName}.value.equals(value)) {
                return ${enumClassName};
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
