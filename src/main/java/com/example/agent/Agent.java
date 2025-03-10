package com.example.agent;

import java.lang.instrument.Instrumentation;

    public class Agent {
        public static void premain(String args, Instrumentation inst) {
            System.out.println("Java Agent loaded successfully!");
            initAgent(inst);
        }

        public static void agentmain(String args, Instrumentation inst) {
            System.out.println("Java Agent dynamically attached successfully!");
            initAgent(inst);
        }

        private static void initAgent(Instrumentation inst) {
            System.out.println("Initializing Agent...");
            inst.addTransformer(new APITransformer(), true);
        }
    }