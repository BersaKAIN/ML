#!/usr/bin/env python
#encoding: utf-8

import os, subprocess

dataDirectory = "./Data/"
modelDirectory = "./Models/"
# datasets = ['bio', 'finance', 'vision', 'synthetic_easy', 'synthesis_hard', 'nlp', 'speech']
datasets = ['synthetic_hard']
datatype = ['.train', '.dev']
algorithms = ['logistic_regression']
models = []
for d in datasets:
  for t in datatype:
    for a in algorithms:
      dataFile = dataDirectory + d + t
      ID = d + t
      cmd = 'java -cp ./lib/library.jar:./lib/commons-cli-1.2.jar:. cs475.Classify -mode train -algorithm {0} -model_file ./Models/{1}.model -data {2}'.format(a, ID, dataFile)
      print cmd
      subprocess.call(cmd, shell=True)
      models.append((ID, dataDirectory+d))

for m in models:
  cmd = 'java -cp ./lib/library.jar:./lib/commons-cli-1.2.jar:. cs475.Classify -mode test -model_file ./Models/{0}.model -data ./Data/{0} -predictions_file ./Preds/{0}.predictions'.format(m[0])
  print cmd
  subprocess.call(cmd, shell=True)

# for m in models[:-8]: #skip the last 8 models since easy and hard do not have test data to run
#   cmd = 'java -cp ./lib/library.jar:./lib/commons-cli-1.2.jar:. cs475.Classify -mode test -model_file {0} -data {1}.test -predictions_file {0}.test.predictions'.format(m[0], m[1])
#   print cmd
#   subprocess.call(cmd, shell=True)

print 'Done'