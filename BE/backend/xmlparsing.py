import xml.etree.ElementTree as ET


bothHandsli=[]
eyesightli=[]
handWorkli=[]
liftPowerli=[]
talkli=[]
stnwalkli=[]


# XML 파일을 열고 파싱
with open("response_1722955414313.xml", 'rb') as xml_file:
    tree = ET.parse(xml_file)
    root = tree.getroot()

# XML 데이터 접근 및 출력
for item in root.findall('.//item'):
    bothHands = item.find('envBothHands').text if item.find('envBothHands') is not None else None
    eyesight = item.find('envEyesight').text if item.find('envEyesight') is not None else None
    handWork = item.find('envHandWork').text if item.find('envHandWork') is not None else None
    liftPower = item.find('envLiftPower').text if item.find('envLiftPower') is not None else None
    talk = item.find('envLstnTalk').text if item.find('envLstnTalk') is not None else None
    stnwalk = item.find('envStndWalk').text if item.find('envStndWalk') is not None else None
    bothHandsli.append(bothHands)
    eyesightli.append(eyesight)
    handWorkli.append(handWork)
    liftPowerli.append(liftPower)
    talkli.append(talk)
    stnwalkli.append(stnwalk)

print(set(bothHandsli))

print(set(eyesightli))

print(set(handWorkli))

print(set(liftPowerli))

print(set(talkli))

print(set(stnwalkli))
