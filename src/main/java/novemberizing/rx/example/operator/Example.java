package novemberizing.rx.example.operator;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public class Example {
    public static void main(String[] args){
        Log.disable(Log.FLOW | Log.HEADER);
        Log.i("just > ", "===============================================");
        novemberizing.rx.example.operator.just.Example.main(args);
        Log.i("sync > ", "===============================================");
        novemberizing.rx.example.operator.sync.Example.main(args);
        Log.i("chain > ", "==============================================");
        novemberizing.rx.example.operator.chain.Example.main(args);
        Log.i("async(async) > ", "=======================================");
        novemberizing.rx.example.operator.async.async.Example.main(args);
        Log.i("async(sync) > ", "========================================");
        novemberizing.rx.example.operator.async.sync.Example.main(args);
        Log.i("completion.port > ", "====================================");
        novemberizing.rx.example.operator.completion.port.Example.main(args);
        Log.i("if else > ", "============================================");
        novemberizing.rx.example.operator.ifelse.Example.main(args);
        Log.i("switch case > ", "=========================================");
        novemberizing.rx.example.operator.switchcase.Example.main(args);
        Log.i("while(op) > ", "===========================================");
        novemberizing.rx.example.operator.whileloop.Example.main(args);
        Log.i("do(op)while > ", "=========================================");
        novemberizing.rx.example.operator.dowhileloop.Example.main(args);
    }
}