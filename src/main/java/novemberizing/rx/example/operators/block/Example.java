package novemberizing.rx.example.operators.block;

import novemberizing.rx.operators.Block;
import novemberizing.rx.Func;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscribers;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
public class Example {

    public static void main(String[] args){
        Operator<String, Integer> stoi = Operator.Op(new novemberizing.rx.Func<String, Integer>() {
            @Override
            public Integer call(String o) {
                return Integer.parseInt(o) + 1;
            }
        });

        Func<Integer, String> f = new novemberizing.rx.Func<Integer, String>() {
            @Override
            public String call(Integer o) {
                return Integer.toString(o);
            }
        };

        stoi.subscribe(Subscribers.Just("op(stoi) 1>"));

        Operator<Integer, String> itos = Operator.Op(new novemberizing.rx.Func<Integer, String>() {
            @Override
            public String call(Integer o) {
                return Integer.toString(o) + "1";
            }
        });

        itos.subscribe(Subscribers.Just("op(stoi) 2>"));


        {
            Block<String, String> block = new Block<>();
            block.start().append(stoi).append(itos).append(stoi).end(itos).subscribe(Subscribers.Just("block 3>"));
            block.exec("1").subscribe(Subscribers.Just("completion(block(task)) 4>"));
            block.foreach(args).subscribe(Subscribers.Just("completion(block(task)) 5>"));

            itos.exec(1);
        }

        {
            Block<String, String> block = new Block<>();
            block.start().append(stoi).append(itos).append(stoi).end(f).subscribe(Subscribers.Just("block 6>"));
            block.exec("1").subscribe(Subscribers.Just("completion(block(task)) 7>"));

            block.foreach(args).subscribe(Subscribers.Just("completion(block(task)) 8>"));

            itos.exec(1);
        }
    }
}
