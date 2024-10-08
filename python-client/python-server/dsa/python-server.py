import socket
import time
import multiprocessing
import socket
import time

import os
import time
import copy
import random
import math

import numpy as np
import pandas as pd

import torch
import torch.nn as nn
import torch.nn.functional as F
from torch.utils.data import TensorDataset, DataLoader, Subset

from torchvision import datasets
from torchvision import transforms

import matplotlib.pyplot as plt
from PIL import Image

import model

device = 'cuda' if torch.cuda.is_available() else 'cpu'


# ResNet18 모델


server_address = ('localhost', 9090)
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(server_address)


def deploy_weight():
    startmsg = "start\n"
    s.sendall(startmsg.encode('utf-8'))
    print("deploy")

# 모델 평가


def validate(model, test_loader):
    model = model.to(device)
    model.eval()
    correct = 0
    total = 0
    with torch.no_grad():
        for (t, (x, y)) in enumerate(test_loader):  # 테스트 데이터 셋(아직 없음, CIFAR10 테스트 로더 그냥 가지고 와서 하면 됨)
            x = x.to(device)
            y = y.to(device)
            out, _ = model(x)
            correct += torch.sum(torch.argmax(out, dim=1) == y).item()
            total += x.shape[0]
    return correct/total


def average_weight(global_model):

    print("average_weight")
    filename = s.recv(1024)
    print(filename.decode("utf-8'"))

    running_avg = None
    train_client_id = [1]
    # load and 평균화
    client_model = torch.load(
        '/mnt/parameters/client_model_{}'.format(train_client_id[0]))
    for k in range(len(train_client_id)):  # 받은 클라이언트 갯수만큼.....
        client_model = torch.load('client_model_{}'.format(train_client_id[k]))
        running_avg = running_model_avg(
            running_avg, client_model.state_dict(), 1/len(train_client_id))
    global_model.load_state_dict(running_avg)
    torch.save(global_model, '/mnt/parameters/global_model.pt')

    filename = "global_model.pt\n"
    s.sendall(filename.encode('utf-8'))


def send_end_message():
    msg = "end"
    print(msg)
    s.sendall(msg.encode('utf-8'))


def running_model_avg(current, next, scale):
    if current == None:
        current = next
        for key in current:
            current[key] = current[key] * scale
    else:
        for key in current:
            current[key] = current[key] + (next[key] * scale)
    return current


if __name__ == "__main__":

    model = model.resnet18(num_classes=10, grayscale=False)
    torch.save(model, '/mnt/parameters/global_model.pt')

    for t in range(100):  # 라운드(100)
        print("Starting Round : {}".format(t+1))

        deploy_weight()
        average_weight(model)

    send_end_message()
