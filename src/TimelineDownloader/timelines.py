class TimelinesDownloader:
    def __init__(self, countries_dir, users_dir, dst_dir, t_key, t_secret, ta_token, ta_secret, language):
        self._countries_dir = countries_dir
        self.dst_dir = dst_dir
        self.users_dir = users_dir
        self.t_key = t_key
        self.t_secret = t_secret
        self.ta_token = ta_token
        self.ta_secret = ta_secret
        self.language = language

    def download(self, numTweets, verbose=False):
        from subprocess import call
        from os import listdir
        from os.path import isfile, isdir, join

        countries_f = open(self._countries_dir)
        countries = countries_f.readlines()
        countries = [country[:-1] for country in countries]
        countries_f.close()


        call(['mkdir', '-p', self.dst_dir])

        for country in countries:
            info_dir = '{}{}.txt'.format(self.users_dir, country)

            info_f = open(info_dir)
            users = info_f.readlines()
            users = [u[:-1] for u in users]
            user_dst = '{}{}/'.format(self.dst_dir, country)
            call(['mkdir', '-p', user_dst])
            user_files = [f for f in listdir(user_dst) if isfile(join(user_dst, f)) and f[-3:] == 'twt'] 
            
            for user in users:
                data = user.split()
                screen_name = data[0]
                uID = data[1]
                us_country = data[2]

                assert us_country == country
                
                command = [
                    'java', '-Xmx7000m', '-Xms2500m', '-jar', 
                    'TimelineDownloader/TWTimelineCrawler/dist/TWTimelineCrawler.jar',
                    '-nt', str(numTweets), 
                    '-u', screen_name, 
                    '-lm', 'download/profiles/',
                    '-t_key', self.t_key,
                    '-t_secret', self.t_secret,
                    '-ta_token', self.ta_token,
                    '-ta_secret', self.ta_secret,
                    '-l', self.language
                ]

                #java -jar TWTimelineCrawler.jar $u_name > $dst_dir/$c/${u_name}_${uID}.twt

                exists = len([f for f in user_files if uID in f]) > 0
                user_f = '{}{}_{}.twt'.format(
                    user_dst,
                    screen_name,
                    uID
                    )
                
                if not exists:
                    dst_f = open(user_f, 'a')
                    call(command, stdout=dst_f)
                    dst_f.close()
                else:
                    print('User {} (@{}) already downloaded.'.format(uID, screen_name))

