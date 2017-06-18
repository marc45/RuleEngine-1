package RuleEngine;

import org.json.JSONArray;
import org.json.JSONObject;

import RuleEngine.BaseOperation.Expression;
import RuleEngine.BaseOperation.Operation;
import RuleEngine.LogicOperation.AbsLogicOperation;

/**
 * Created by ShengYang on 2017/2/23.
 */
public class ExpressionParser {
    private static final OperationManager operatons = OperationManager.INSTANCE;

    public static final String SYMBOL_RESULT = "result";

    public static Expression parse(JSONObject root) {
        if (root == null) {
            return null;
        }
        JSONArray childOp = root.names();
        String operationName = null;
        JSONObject jsonObject = null;
        Expression expression = null;
        for (int i = 0; i < childOp.length(); i++) {
            operationName = childOp.optString(i);
            if (operationName.equals(SYMBOL_RESULT)) {
                continue;
            }

            jsonObject = root.optJSONObject(operationName);
            if (jsonObject == null) {
                return null;
            }

            expression = operatons.getOperation(operationName);

            if (operatons.getOperation(operationName) != null
                    && operatons.isLogicalOperation(operatons.getOperation(operationName))) {
                ((AbsLogicOperation)expression).parseData(jsonObject);
                parse(jsonObject);
            } else {
                Operation operation = operatons.getOperation(operationName);
                if (operation != null) {
                    operation.parseData(jsonObject);
                }
            }
        }

        return expression;
    }
}
