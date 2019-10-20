from flask import Flask, render_template, request, jsonify, url_for, send_from_directory
import numpy as np
from flask_restful import reqparse, abort, Api, Resource
from keras.models import load_model
from utilities_digit_recognize import predict_digit, get_image
import fastai
from fastai.vision import *
import timm
import os
from utilities_aptos import save_image, get_heatmap
from utilities_skin import *
from flask_cors import CORS

from rake_nltk import Rake

app = Flask(__name__, static_url_path='/heatmap')
cors = CORS(app, resources={r"/*": {"origins": "*"}})
app.config["CLIENT_IMAGES"] = "/home/rishabh/PycharmProjects/Medidoc/heatmap"

# def options (self):
#     return {'Allow' : 'POST' }, 200, \
#     { 'Access-Control-Allow-Origin': '*', \
#       'Access-Control-Allow-Methods' : 'PUT,GET,POST,OPTIONS' }
@app.route("/")
def index():
    return render_template("index.html")


@app.route("/digit/", methods=['POST'])
def predict():
    model = load_model('mnist_model.h5')

    if request.method == 'POST':
        digit_name = request.get_json()
        comment = digit_name['url']
        img = get_image(comment)
        print(comment)
        ans = predict_digit(img, model)

    # return render_template('results.html', prediction=my_prediction, comment=comment)
    return jsonify({'result': int(ans)})

@app.route("/get-image/<image_name>")
def get_image(image_name):
    try:
        return send_from_directory(app.config["CLIENT_IMAGES"], filename=image_name, as_attachment=True)
    except FileNotFoundError:
        abort(404)


@app.route("/keyword/", methods=['POST'])
def predict_digits():
    if request.method == 'POST':
        transcript = request.get_json()
        script = transcript['data']
        r = Rake()
        r.extract_keywords_from_text(script)
        predicted_ans = ''.join(r.get_ranked_phrases())

    return jsonify({'result': predicted_ans})


if __name__ == '__main__':
    app.run(host="10.42.0.1", port=5000, debug=True)
