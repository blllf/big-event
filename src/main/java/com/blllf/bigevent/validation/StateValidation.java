package com.blllf.bigevent.validation;

import com.blllf.bigevent.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StateValidation implements ConstraintValidator<State , String> {
    /**
     *
     * @param s 将来要校验的数据
     * @param constraintValidatorContext
     * @return  如果返回true校验通过
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        //提供校验规则
        if (s == null){
            return false;
        }
        if (s.equals("已发布") || s.equals("草稿")){
            return true;
        }
        return false;
    }
}
